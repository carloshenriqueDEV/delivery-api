package com.deliverytech.delivery_api.config;

import brave.Tracer;
import brave.Tracing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

    @Bean
    public Tracer tracer() {
        return Tracing.newBuilder().build().tracer();
    }
}