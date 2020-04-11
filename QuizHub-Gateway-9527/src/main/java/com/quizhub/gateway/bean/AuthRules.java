package com.quizhub.gateway.bean;

import com.quizhub.gateway.service.AuthService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Getter
    private List<AuthRule> rules = new ArrayList<>();


    public static AuthRules getInstance() {
        return instance;
    }

    /**
     * 单独的一条权限规则
     */
    public static class AuthRule {
        public String antMatcher;
        public List<String> authorities;
        public Boolean authenticated;

        private AuthRule(FinalBuilder builder) {
            antMatcher = builder.antMatcher;
            authenticated = builder.authenticated;
            authorities = builder.authorities;
        }
    }


    /**
     * 最后一级建造者，可以继续再生产规则，或者结束返回
     */
    public static class FinalBuilder {
        private String antMatcher;
        private List<String> authorities;
        private Boolean authenticated;

        FinalBuilder(String antMatcher, String[] authorities, Boolean authenticated) {
            this.antMatcher = antMatcher;
            this.authenticated = authenticated;

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

        RuleBuilder(String antMatcher) {
            this.antMatcher = antMatcher;
        }

        /**
         * 需要权限，则进入三级建造者：权限建造者
         *
         * @return
         */
        public FinalBuilder authenticated() {
            return new FinalBuilder(antMatcher, null, true);
        }

        /**
         * 不需要建造者，直接完工
         *
         * @return
         */
        public FinalBuilder permitAll() {
            return new FinalBuilder(antMatcher, null, false);
        }

        /**
         * 不需要建造者，直接完工
         *
         * @return
         */
        public FinalBuilder checkAuthority(String... auths) {
            return new FinalBuilder(antMatcher, auths, true);
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




}
