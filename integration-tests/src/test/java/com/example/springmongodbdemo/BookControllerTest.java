package com.example.springmongodbdemo;

import io.github.vkn.spring.mongodb.unit.MongoDbQueryTest;
import io.github.vkn.spring.mongodb.unit.MongoDbUnitExtension;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

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
    void notApplied() {
        given()
                .when().get("/books")
                .then()
                .statusCode(200)
                .body(not(empty()));
    }

    @Test
    @MongoDbQueryTest
    void defaults() {
        given()
                .when().get("/books")
                .then()
                .statusCode(200)
                .body(not(empty()));
    }

    @Test
    @MongoDbQueryTest(collection = "my-collection")
    void collectionOnly() {
        given()
                .when().get("/books")
                .then()
                .statusCode(200)
                .body(not(empty()));
    }

    @Test
    @MongoDbQueryTest(exactly = 1, collection = "book")
    void exactly() {
        given()
                .when().get("/books")
                .then()
                .statusCode(200)
                .body(not(empty()));
    }

    @Test
    @MongoDbQueryTest(exactly = 1, commandName = "find")
    void exactlyCommand() {
        given()
                .when().get("/books")
                .then()
                .statusCode(200)
                .body(not(empty()));
    }

    @Test
    @MongoDbQueryTest(exactly = 0, commandName = "delete")
    void exactlyCommandZero() {
        given()
                .when().get("/books")
                .then()
                .statusCode(200)
                .body(not(empty()));
    }

    @Test
    @MongoDbQueryTest(exactly = 1, commandName = "delete")
    void exactlyCommandDeleteONe() {
        TestUtils.deleteBooks("/books");
    }

    @Test
    @MongoDbQueryTest(exactly = 0, commandName = "find")
    void deleteNotFind() {
        TestUtils.deleteBooks("/books");
    }

    @Test
    @MongoDbQueryTest(exactly = 0, commandName = "find")
    @MongoDbQueryTest(exactly = 1, commandName = "delete")
    void repeatedAnnotation() {
        TestUtils.deleteBooks("/books");
    }

    @Test
    @MongoDbQueryTest(exactly = 1, commandName = "delete")
    @MongoDbQueryTest(exactly = 0, commandName = "find")
    @MongoDbQueryTest(exactly = 1)
    void repeatedAnnotation2() {
        TestUtils.deleteBooks("/books");
    }

    @Test
    @MongoDbQueryTest(commandName = "delete")
    void commandOnlySpecified() {
        given()
                .when().get("/books")
                .then()
                .statusCode(200)
                .body(not(empty()));
    }

    @Test
    @MongoDbQueryTest(exactly = 1, collection = "book")
    void atMost() {
        given()
                .when().get("/books")
                .then()
                .statusCode(200)
                .body(not(empty()));
    }

    @Test
    @MongoDbQueryTest(atLeast = 1, collection = "book")
    void atLeast() {
        given()
                .when().get("/books")
                .then()
                .statusCode(200)
                .body(not(empty()));
    }

    @Test
    @MongoDbQueryTest(atMost = 2, collection = "book")
    void atMostWithGap() {
        given()
                .when().get("/books")
                .then()
                .statusCode(200)
                .body(not(empty()));
    }

    @Test
    @MongoDbQueryTest(exactly = 0, collection = "book")
    void countWithError() {
        given()
                .get("/books/invalid")
                .then()
                .assertThat()
                .statusCode(400);
    }
}
