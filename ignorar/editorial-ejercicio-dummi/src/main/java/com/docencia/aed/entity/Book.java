package com.docencia.aed.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
/**
 * @author danielrguezh
 * @version 1.0.0
 */
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonBackReference
    private Author author;

    /**
     * Cosntructor vacio
     */
    public Book() {
    }
    /**
     * Constructor con id
     * @param id del libro 
     */
    public Book(Long id) {
        this.id = id;
    }
    /**
     * Constructor con propiedades
     * @param id unica del libro 
     * @param title del libro 
     * @param publicationYear del libro 
     * @param author del libro 
     */
    public Book(Long id, String title, Integer publicationYear, Author author) {
        this.id = id;
        this.title = title;
        this.publicationYear = publicationYear;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Book)) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(id, book.id) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
}
