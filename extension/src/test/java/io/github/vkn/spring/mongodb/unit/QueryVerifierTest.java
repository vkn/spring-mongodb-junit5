package io.github.vkn.spring.mongodb.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class QueryVerifierTest {

    @Test
    void verify() throws Exception {
        Method method = TestedClass.class.getDeclaredMethod("someTest");
        MongoDbUnitCommandListener listener = new MongoDbUnitCommandListener();
        QueryVerifier.verify(method, listener);
    }

    @Test
    void failing() throws Exception {
        Method method = TestedClass.class.getDeclaredMethod("someFailingTest");
        MongoDbUnitCommandListener listener = new MongoDbUnitCommandListener();
        Assertions.assertThatThrownBy( () -> QueryVerifier.verify(method, listener))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Mongodb extension requires exactly 1 commands, but count is 0");

    }

    @Test
    void repeatedAnnotation() throws Exception {
        Method method = TestedClass.class.getDeclaredMethod("someTestWithRepeatedAnnotation");
        MongoDbUnitCommandListener listener = new MongoDbUnitCommandListener();
        QueryVerifier.verify(method, listener);
    }

    @Test
    void noAnnotation() throws Exception {
        Method method = TestedClass.class.getDeclaredMethod("noAnnotation");
        MongoDbUnitCommandListener listener = new MongoDbUnitCommandListener();
        QueryVerifier.verify(method, listener);
    }

    @SuppressWarnings("JUnitMalformedDeclaration")
    private static class TestedClass {

        @Test
        @MongoDbQueryTest(exactly = 1)
        void someFailingTest() {

        }

        @Test
        @MongoDbQueryTest(exactly = 0)
        void someTest() {

        }

        @Test
        @MongoDbQueryTest(exactly = 0, commandName = "delete")
        @MongoDbQueryTest(exactly = 0, commandName = "find")
        void someTestWithRepeatedAnnotation() {

        }

        @Test
        void noAnnotation() {}
    }
}