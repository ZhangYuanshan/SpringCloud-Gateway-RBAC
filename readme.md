[TOC]

# 前言

> 本来是打算延用shiro到微服务上来做RBAC的，但是似乎springcloud系列对shiro不太友好，所以被迫去学习了下Spring Security，打算搭建一个UAA服务器，使用oauth2和jwt来做认证，然后把网关作为资源服务器来统一处理请求，解析jwt判断权限，从而使得微服务内部的各个模块从此不用关心权限问题
>
> 一开始UAA服务器很愉快的就搭建好了，但是，但是，但是，我tm真的没想到Spring Security那么烦
>
> 由于我的网关是使用Spring Gateway搭建的，底层用的是WebFlux而不是SpringMVC，所以Spring Security的资源服务器的写法就变了....
>
> 大概去官方文档上查了一下，杂七杂八引入各种依赖然后又是jwk什么什么的，反正就是，SpringMVC里一个@EnableResourceServer搞定的事情他扯半天都解决不了而且又是表单登录又是自定会失败处理器，感觉事情被过于复杂化了....（或者是因为我太菜了...）
>
> 所以最后决定，在网关阶段就不去使用SpringSecurity了，自己利用UAA颁发的jwt配合filter去写一套RBAC机制......



------------------------------------------

# 前期准备

-------------------

## UAA的搭建

这个不是重点，我放在文章最后讲...

总之，我就是先搭建好了一台UAA服务器，能支持oauth2，然后token采用的是jwt，然后提供了一个额外的用户的账号密码登录接口（实际上是用另外一个微服务包装了一下然后远程调用的）

正确了就返回jwt和refresh_token

同时，UAA还提供了一个jwt的校验接口，所以网关校验jwt就直接去找UAA了

-------------------

## UAA的token格式

这个是UAA服务器颁发的jwt的格式，我做RBAC就是解析authorities字段后获取里面的权限内容即可

![image-20200328230236066](https://pic.imlehr.com/uploads/typora/image-20200328230236066.png)

--------------------

## 网关的搭建

没什么好说的，就是一个Gateway的搭建过程，具体步骤可以去看我之前写的博客

然后就是，所有的模块包括UAA再去nacos里注册一下，完了

--------------------------

# 整体设计思路

个人的设计里，是打算把所有对外接口的权鉴都在网关统一处理了，使得每个微服务模块就只用专心于自己的业务

网关模块不用集成Spring Security

![image-20200328231822114](https://pic.imlehr.com/uploads/typora/image-20200328231822114.png)



网关要做的事情，思路如下：

- 从请求里获取jwt
- 找UAA服务器校验jwt（因为UAA提供了一个校验接口）
- RBAC，判断权限
- 把jwt里有用的信息放入请求头里
- 放行

# 具体步骤

我先放一下我网关模块的项目结构：

![image-20200328233641484](https://pic.imlehr.com/uploads/typora/image-20200328233641484.png)

其中，CorsConfig是跨域设置，不讲了，然后ConfigBean是注入restTemplate的，也不讲了

JwtFilter，AuthRules，AuthService是重点（ConfigRule是注入RBAC规则的）

-------------------

## 网关过滤器

我实现的代码具体是在过滤器里完成的，涉及到的其他部分会在后续讲到，所以，先来看一下总体思路：

代码如下：

```java

/**
 * @author Lehr
 * @create: 2020-03-24
 */
@Component
@Slf4j
public class JwtFilter implements GlobalFilter, Order {

    @Autowired
    private AuthService authService;

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //从请求头里获取token
        String token = exchange.getRequest().getHeaders().getFirst("token");
        //从请求头里获取路径
        String uri = exchange.getRequest().getURI().getPath();
        //校验jwt的合法性，如果没有jwt或者失效了，都返回null
        JSONObject jsonJwt = authService.parseJwt(token);

        //判断当前用户是否有权限访问资源
        if(authService.hasAuth(uri,jsonJwt))
        {
            log.info("Auth checked!");
            //添加参数
            Consumer<HttpHeaders> httpHeaders = h -> h.set("username", jsonJwt.get("username").toString());
            ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().headers(httpHeaders).build();
            exchange.mutate().request(serverHttpRequest).build();
            //完成
            return chain.filter(exchange);
        }

        //权利不足，返回提示，有可能是权限不够或者没有登录或者token到期造成的
        log.info("Unauthorized! Request is denied!");
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        JSONObject message = new JSONObject();
        message.put("success", false);
        message.put("errCode", "401");
        message.put("errMsg", "没有权利");
        message.put("data", null);
        byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int value() {
        return 0;
    }
}

```

这里我的规定是，以后用户需要把jwt放在请求头的token里传过来（哈哈因为反正是我自己检查）

上面这段代码的逻辑总结一下就是：

- 获取jwt
  - 从token里直接拿到
- 使用`authService.parseJwt`方法来解析jwt，具体内容看下文
  - 如果没有jwt或者jwt不合法或者过期了，则返回的是null
  - 如果jwt是有效的，则这里返回的就是jwt解析后的内容，也就是我上文贴图里的那个
- 使用`authService.hasAuth`来判断当前用户是否有权限访问
  - 如果有权限访问，就把jwt的username字段放入请求头（这只是个示意）
  - 如果没有权限，则准备好报错信息，然后401返回

----------

这其实就是我总体的思路的体现了，具体的细节都被封装在了authService里，所以接下来看authService

## 细节处理

先放一下authService的代码：

```java


/**
 * @author Lehr
 * @create: 2020-03-28
 */
@Service
public class AuthService {

    @Autowired
    private AuthRules authRules;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 解析成功就返回，不然就是null
     * @param token
     * @return
     */
    public JSONObject parseJwt(String token) {
        if(token==null)
        {
            return null;
        }
        try{
            //调用uaa模块检查合法性
            ServiceInstance serviceInstance = loadBalancerClient.choose("QuizHub-UAA");
            String path = String.format("http://%s:%s/oauth/check_token?token={token}", serviceInstance.getHost(), serviceInstance.getPort());
            String jsonToken = restTemplate.getForObject(path, String.class, token);
            return JSONObject.parseObject(jsonToken);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public boolean hasAuth(String uri,JSONObject json)
    {
        //检查认证是否有效，就是看json是不是为空，等效于看你登录了没有
        Boolean isAuthenticated = json!=null;
        //别问我为什么是0，我就是这样设计的
        List<String> auths = new ArrayList<>(0);
        //获取权限，转化为字符串List
        if(json!=null)
        {
            JSONArray jsonArray = json.getJSONArray("authorities");
            String[] auts = new String[jsonArray.size()];
            jsonArray.toArray(auts);
            auths = Arrays.asList(auts);
        }

        //进行权限检查
        return authRules.isAuthorized(uri,auths,isAuthenticated);
    }



}
```

### 解析jwt

没什么说的，主要就是：

```java
   ServiceInstance serviceInstance = loadBalancerClient.choose("QuizHub-UAA");
            String path = String.format("http://%s:%s/oauth/check_token?token={token}", serviceInstance.getHost(), serviceInstance.getPort());
            String jsonToken = restTemplate.getForObject(path, String.class, token);
            return JSONObject.parseObject(jsonToken);
```

因为spring-security-oauth2搭建的UAA服务器是可以开放一个uri是`/oauth/check_token`的接口作为jwt检验的

所以这里就用nacos发现服务提供的带负载均衡的restTemplate直接去get结果就好了

至于我这里加了try-catch，是因为，如果解析失败了的话他就会报一个400（暂时懒得去UAA写handler所以就这里Try-catch处理了）

### RBAC

其实这里也封装的很好了，读者可发现，其实重点就在最后一句话：

```java
return authRules.isAuthorized(uri,auths,isAuthenticated);
```

前面的都是准备参数

这个authRules的话，就是重点了......

-----------------------------

## AuthRules

可能读者会觉得似乎没有见过这个东西

对，因为这个辣鸡RBAC控制器是我自己乱写的......也是我这篇博客里最想讲的地方.....

通过在ConfigRules里进行简单的配置，就能实现（简单的）权限控制

---------------------------

### 配置AuthRules

首先来看一下ConfigRules

```java
@Configuration
public class ConfigRule {
    @Bean
    public AuthRules authRules()
    {
        return AuthRules.getBuilder()
                .antMatchers("/users/test").authenticated().hasAuthority("ROLE_USER")
                .and()
                .antMatchers("/order/**").permitAll()
                .and()
                .antMatchers("/security/**").hasAuthority("admin")
                .build();
    }
}
```

嗯，这熟悉的感觉，就和Spring Security里的一样

是的，虽然我觉得Spring Security很烦，但是他这种写权限控制的方法我挺喜欢的，所以就自己实现了一个类，通过这样类似的配置，就能实现权限控制

其中我介绍一下几个规则：

- 一条规则必须以antMatchers开头，括号里只能接一条pattern
- 后面接authenticated代表需要登录，然后可以再接hasAuthority，也就是指定权限
- 也可以直接permitAll全部放行
- 也可以直接hasAuthority直接指定权限
- 如果想要继续写下一条规则，则需要用到`and()`
- `and()`代表，后面还有规则
- `build()`代表，规则全部写完了

反正我觉得，这样写起来挺舒服的....虽然现在约束能力比较弱

接下来看核心代码：

------------------------------

### AuthRules类

虽然我给这个类准备的调用方法是类似spring security里的，但是实际上我并没有太参考他的代码（因为他的代码太复杂了一层调一层......），只是很简单的用了一个建造者模式去实现（具体表不标准我也不知道，今天是第一天细看建造者模式.....或者，就算这个不是建造者模式，也别吐槽，反正就那个意思.....）

老规矩贴代码：(还不是写的很完善，只是能跑而已....)

```java
package com.imlehr.quizhub.gateway.bean;

import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-03-28
 */
public class AuthRules {

    private static AuthRules instance = new AuthRules();

    private static AntPathMatcher antPathMatcher = new AntPathMatcher();

    private List<AuthRule> rules = new ArrayList<>();


    public static AuthRules getInstance() {
        return instance;
    }

    /**
     * 单独的一条权限规则
     */
    public static class AuthRule {
        private String antMatcher;
        private List<String> authorities;
        private Boolean authenticated;
        private Boolean permitAll;

        private AuthRule(FinalBuilder builder) {
            antMatcher = builder.antMatcher;
            authenticated = builder.authenticated;
            authorities = builder.authorities;
            permitAll = builder.permitAll;
        }
    }


    /**
     * 三级建造者，负责权限
     */
    public static class AuthBuilder {
        private String antMatcher;

        AuthBuilder(String antMatcher) {
            this.antMatcher = antMatcher;
        }

        public FinalBuilder hasAuthority(String... auths) {
            return new FinalBuilder(antMatcher, auths, true, false);
        }
    }

    /**
     * 最后一级建造者，可以继续再生产规则，或者结束返回
     */
    public static class FinalBuilder {
        private String antMatcher;
        private List<String> authorities;
        private Boolean authenticated;
        private Boolean permitAll;

        FinalBuilder(String antMatcher, String[] authorities, Boolean authenticated, Boolean permitAll) {
            this.antMatcher = antMatcher;
            this.authenticated = authenticated;
            this.permitAll = permitAll;

            if (authorities == null) {
                this.authorities = new ArrayList<>(0);
            } else {
                this.authorities = Arrays.asList(authorities);
            }

        }

        public UriBuilder and() {
            getInstance().rules.add(new AuthRule(this));
            return new UriBuilder();
        }

        public AuthRules build() {
            getInstance().rules.add(new AuthRule(this));
            return getInstance();
        }
    }


    /**
     * 二级建造者，负责判断需不需要权限
     */
    public static class RuleBuilder {
        private String antMatcher;
        private List<String> authorities = new ArrayList<>();

        RuleBuilder(String antMatcher) {
            this.antMatcher = antMatcher;
        }

        /**
         * 需要权限，则进入三级建造者：权限建造者
         *
         * @return
         */
        public AuthBuilder authenticated() {
            return new AuthBuilder(antMatcher);
        }

        /**
         * 不需要建造者，直接完工
         *
         * @return
         */
        public FinalBuilder permitAll() {
            return new FinalBuilder(antMatcher, null, false, true);
        }

        /**
         * 不需要建造者，直接完工
         *
         * @return
         */
        public FinalBuilder hasAuthority(String... auths) {
            return new FinalBuilder(antMatcher, auths, true, false);
        }

    }


    /**
     * 一级建造者只负责uri
     */
    public static class UriBuilder {
        public RuleBuilder antMatchers(String antPattern) {
            return new RuleBuilder(antPattern);
        }
    }


    /**
     * 拿到一级建造者
     *
     * @return
     */
    public static AuthRules.UriBuilder getBuilder() {
        return new UriBuilder();
    }

    public Boolean isAuthorized(String uri, List<String> auths, Boolean isAuthenticated) {
        for (AuthRule rule : rules) {
            //如果不是全部放行的
            if (!rule.permitAll) {
                //如果能匹配上，进行后续操作
                if (antPathMatcher.match(rule.antMatcher, uri)) {
                    //如果需要权限验证
                    if (rule.authenticated) {
                        //如果未登录，则失败
                        if (!isAuthenticated) {
                            return false;
                        }
                        //rule里每个里，都xxx
                        for (String authority : rule.authorities) {
                            if (!auths.contains(authority)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
```



AuthRules类是一个代表一系列RBAC规则集合的类，里面有一个List用于保存每个规则（AuthRule子类）

每个规则（AuthRule）里有几个属性，分别是

- antMatcher：匹配规则，ant风格
- authorities：需要的权限，一系列String数组（这里我就没有太去区分是Role还是Resource了
- authenticated：是否需要登录才能访问
- permitAll：是否可以直接放行（其实我觉得这个都没必要写，和authenticated一个意思其实）

------------

首先我想讲一下` isAuthorized`方法

这个方法是一个用于比较某次请求是否有权利访问目标uri的一个工具方法

也就是在AuthService里用到的那个

思路也比较简单，就是用传入的uri找到匹配的规则，然后比对一下规则是否需要登录，当前用户的权限够不够....这里的权限判断我写的比较粗糙，就是，需要全部权限都满足才放行

---------------

然后是，创建方法：

为了实现Spring Security的那种链式编程的玩法，一开始我想的是直接用lombok的标签，但是后来发现，如果那样的话，可能就会让用户不停的antMatcher.antMatcher了

所以就还是用了多级建造者的方法来写了一下，具体看代码，大概调用思路如下图：

![image-20200329001423976](https://pic.imlehr.com/uploads/typora/image-20200329001423976.png)

done

最后，附上项目github地址：

[https://github.com/Lehr130/SpringCloud-Gateway-RBAC](https://github.com/Lehr130/SpringCloud-Gateway-RBAC)