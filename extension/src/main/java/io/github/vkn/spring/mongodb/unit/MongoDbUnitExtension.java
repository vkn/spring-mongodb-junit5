package io.github.vkn.spring.mongodb.unit;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MongoDbUnitExtension extends SpringExtension {
    MongoDbUnitCommandListener mongoDbUnitCommandListener;

    private static final ConcurrentHashMap<String, MongoDbUnitCommandListener> STORE = new ConcurrentHashMap<>();

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        super.beforeTestExecution(context);
        mongoDbUnitCommandListener = getApplicationContext(context).getBean(MongoDbUnitCommandListener.class);
        Objects.requireNonNull(mongoDbUnitCommandListener,
                "MongoDbUnitCommandListener is not available, please read to documentation how to fix");
        mongoDbUnitCommandListener.start();
        STORE.put(context.getRequiredTestMethod().getName(), mongoDbUnitCommandListener);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        super.afterTestExecution(context);
        MongoDbUnitCommandListener listener = STORE.remove(context.getRequiredTestMethod().getName());
        QueryVerifier.verify(context.getRequiredTestMethod(), listener);
    }

}
