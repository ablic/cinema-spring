package com.ablic.cinema.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "countries")
@NoArgsConstructor
@Data
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Укажите сокращенное название")
    private String shortName;
    @NotBlank(message = "Укажите полное название")
    private String fullName;
    @ManyToMany
    @JsonBackReference
    private List<Movie> movies;
}
