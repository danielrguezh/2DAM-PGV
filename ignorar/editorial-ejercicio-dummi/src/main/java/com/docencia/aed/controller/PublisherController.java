package com.docencia.aed.controller;

import com.docencia.aed.entity.Publisher;
import com.docencia.aed.service.IPublisherService;
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
@Tag(name = "Publishers API")
@RequestMapping("/")
@SecurityRequirement(name = "bearerAuth") // Indica a Swagger que estos endpoints requieren el esquema 'bearerAuth'
@CrossOrigin
public class PublisherController {
    private final IPublisherService service;

    public PublisherController(IPublisherService service) {
        this.service = service;
    }


    @Operation(summary = "Get all publishers")
    @GetMapping("/publishers")
    public List<Publisher> getAllPublishers() {
        return service.findAll();
    }

    @Operation(summary = "Create publisher")
    @PostMapping("/publishers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Publisher not found")
    })
    public ResponseEntity<Publisher> createPublisher(@Valid @RequestBody Publisher publisher) {
        Publisher created= service.create(publisher);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
