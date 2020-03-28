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
