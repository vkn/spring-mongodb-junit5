package com.example.springmongodbdemo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @DeleteMapping
    public ResponseEntity<Void> clearBooks() {
        bookRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Void> addBook(@RequestBody Book book) {
        bookRepository.save(book);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthor(author);
    }

    // Example of a method that could potentially handle an invalid operation
    @GetMapping("/invalid")
    public ResponseEntity<List<Book>> error() {
        // This should properly handle errors or exceptions as per Spring's capability
        // Example: throw a new ResponseStatusException with appropriate HTTP status code
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operation");
    }
}

