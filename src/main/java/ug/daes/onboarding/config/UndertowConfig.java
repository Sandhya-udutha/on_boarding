package ug.daes.onboarding.config;

import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.undertow.UndertowOptions;

@Configuration
public class UndertowConfig {

    @Bean
    public UndertowServletWebServerFactory undertowServletWebServerFactory() {
        UndertowServletWebServerFactory factory =
                new UndertowServletWebServerFactory();

        factory.addBuilderCustomizers(builder ->
                builder.setServerOption(
                        UndertowOptions.MAX_ENTITY_SIZE,
                        50L * 1024 * 1024
                )
        );

        return factory;
    }
}
