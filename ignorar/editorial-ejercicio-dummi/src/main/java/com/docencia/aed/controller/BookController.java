package com.docencia.aed.controller;

import com.docencia.aed.entity.Book;
import com.docencia.aed.service.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author danielrguezh
 * @version 1.0.0
 */
@RestController
@Tag(name = "Books API")
@RequestMapping("/")
@SecurityRequirement(name = "bearerAuth") // Indica a Swagger que estos endpoints requieren el esquema 'bearerAuth'
@CrossOrigin
public class BookController {
    private final IBookService service;

    public BookController(IBookService service) {
        this.service = service;
    }


    @GetMapping("/books")
    @Operation(summary = "Get all books")
    public List<Book> getAllBooks(@RequestParam(value = "authorId", required = false) Long authorId) {
        return service.findAll(authorId);
    }

    @Operation(summary = "Get book by ID")
    @GetMapping("/books/{id}")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public Book getBookById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Create a book for an author")
    @PostMapping("/books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Book> createBook(@RequestParam("authorId") Long authorId,
                                           @Valid @RequestBody Book book) {
        Book created= service.create(authorId, book);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
