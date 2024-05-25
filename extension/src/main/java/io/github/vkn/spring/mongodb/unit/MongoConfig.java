package io.github.vkn.spring.mongodb.unit;

import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
    @Bean
    public MongoClientSettingsBuilderCustomizer customizeTest(MongoDbUnitCommandListener mongoDbUnitCommandListener) {
        return client -> client.addCommandListener(mongoDbUnitCommandListener);
    }

    @Bean
    public MongoDbUnitCommandListener  mongoDbUnitCommandListener() {
        return new MongoDbUnitCommandListener();
    }
}
