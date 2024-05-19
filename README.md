# Spring Mongodb Junit5 Extension

The extension allows to verify number of executed queries to [mongo db](https://www.mongodb.com/) during 
a [spring-boot](https://spring.io/projects/spring-boot) test execution.


## Installation

Add the dependency in your pom.xml

```xml
<dependency>
    <groupId>io.github.vkn</groupId>
    <artifactId>spring-mongodb-junit5</artifactId>
    <version>0.0.1</version>
    <scope>test</scope>
</dependency>
```


### Usage
To get started, annotate your test class with `@ExtendWith(MongoDbUnitExtension.class)` and a test method with 
`@MongoDbQueryTest`. See `BookControllerTest` in integration-tests module and java docs of `@MongoDbQueryTest` 
for more information. 

Create `MongoDbUnitCommandListener`:
```java
import io.github.vkn.spring.mongodb.unit.MongoDbUnitCommandListener;
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


```

Example test:

```java

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MongoDbUnitExtension.class)
class BookControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        TestUtils.insertBooks("/books");
    }

    @AfterEach
    void tearDown() {
        TestUtils.deleteBooks("/books");
    }

    @Test
    @MongoDbQueryTest(exactly = 1, collection = "my-collection")
    public void exactly() {
        given()
                .when().get("/books")
                .then()
                .statusCode(200)
                .body(not(empty()));
    }
}
```

Repeated annotations are also supported:

```java
@Test
@MongoDbQueryTest(exactly = 1, commandName = "find")
@MongoDbQueryTest(exactly = 0, commandName = "delete")
public void exactly() {
    given()
            .when().get("/books")
            .then()
            .statusCode(200)
            .body(not(empty()));
}
```

### Contributing
Contributions are welcome! If you have suggestions for improvements or encounter any issues,
please feel free to open an issue or submit a pull request.

### License
This project is licensed under the Apache Licence 2.0. See the LICENSE file for more details.

