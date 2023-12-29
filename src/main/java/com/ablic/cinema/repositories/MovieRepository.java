package com.ablic.cinema.repositories;

import com.ablic.cinema.models.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {
    Iterable<Movie> findByNameContainingIgnoreCase(String part);
}
