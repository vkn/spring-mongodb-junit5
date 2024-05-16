package com.example.springmongodbdemo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByAuthor(String author);
}

