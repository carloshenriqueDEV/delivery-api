package com.deliverytech.delivery_api.config;

import brave.Tracing;
import brave.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

    @Bean
    public Tracing tracing() {
        // configuração básica — ajuste conforme ambiente/propósitos de tracing
        return Tracing.newBuilder()
                .localServiceName("delivery-api")
                .build();
    }

    @Bean
    public Tracer braveTracer(Tracing tracing) {
        return tracing.tracer();
    }
}