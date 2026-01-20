package com.docencia.aed.controller;

import com.docencia.aed.entity.Author;
import com.docencia.aed.entity.Book;
import com.docencia.aed.service.IAuthorService;
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
@Tag(name = "Authors API")
@RequestMapping("/")
@SecurityRequirement(name = "bearerAuth") // Indica a Swagger que estos endpoints requieren el esquema 'bearerAuth'
@CrossOrigin
public class AuthorController {
    private final IAuthorService service;

    public AuthorController(IAuthorService service) {
        this.service = service;
    }


    @GetMapping("/authors")
    @Operation(summary = "Get all authors")
    public List<Author> getAllAuthors() {
        return service.findAll();
    }

    @GetMapping("/authors/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    public Author getAuthorById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("/authors")
    @Operation(summary = "Create author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody Author author) {
        Author created= service.create(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/authors/{id}/books")
    @Operation(summary = "Get all books")
    public List<Book> getBooksByAuthor(@PathVariable("id") Long authorId) {
        return service.findBooksByAuthor(authorId);
    }
}
