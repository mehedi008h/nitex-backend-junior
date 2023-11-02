package com.mehedi.nitex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    @NotBlank(message = "Name is required!")
    private String name;
    @NotBlank(message = "Author is required!")
    private String author;
    @NotBlank(message = "Description is required!")
    private String description;
    // many-to-Many relation between user
    @ManyToMany(mappedBy = "collections")
    Set<User> collects;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getCollects() {
        return collects;
    }

    public void setCollects(Set<User> collects) {
        this.collects = collects;
    }
}
