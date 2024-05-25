package io.github.vkn.spring.mongodb.unit;

import org.junit.jupiter.api.Test;

class TestedClass {

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
    @MongoDbQueryTest(exactly = 0, commandName = "delete")
    void someTestWithCommandConstraint() {

    }

    @Test
    void noAnnotation() {
    }
}
