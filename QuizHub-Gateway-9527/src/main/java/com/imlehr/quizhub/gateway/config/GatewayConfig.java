package com.imlehr.quizhub.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lehr
 * @create: 2020-03-24
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator1(RouteLocatorBuilder routeLocatorBuilder)
    {
        RouteLocatorBuilder.Builder routers = routeLocatorBuilder.routes();

        routers.route("GuitarProvider",r->r.path("/guitars").uri("lb://GuitarProvider")).build();

        return  routers.build();
    }


}
