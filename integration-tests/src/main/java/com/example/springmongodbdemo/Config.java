package com.example.springmongodbdemo;

import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class Config {

    @Bean
    @Primary
    public MongoClientSettingsBuilderCustomizer customize() {
        return client -> client.applicationName("foo");
    }
}
