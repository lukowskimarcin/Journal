package com.journal.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {

        @Bean
        public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
                return builder.routes()

                                .route(r -> r.path("/api/v1/candlestick/**")
                                                .uri("lb://candlestick-service"))

                                .route(r -> r.path("/api/v1/mt5/**")
                                                .uri("lb://mt5-service"))
                                
                                .build();
        }
}