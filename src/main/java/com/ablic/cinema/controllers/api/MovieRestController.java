package com.ablic.cinema.controllers.api;

import com.ablic.cinema.helpers.ImageHelper;
import com.ablic.cinema.models.Movie;
import com.ablic.cinema.services.MovieService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieRestController {
    @Autowired
    private MovieService movieService;

    @GetMapping
    public List<Movie> getAll() {
        return movieService.findAll();
    }

    @GetMapping("{id}")
    public Movie getById(@PathVariable Long id) {
        Optional<Movie> movieOptional = movieService.findById(id);

        if (movieOptional.isEmpty()) {
            throw new EntityNotFoundException("Movie not found");
        }

        return movieOptional.get();
    }

    @GetMapping("/poster/{id}")
    public byte[] getPosterById(@PathVariable Long id) throws IOException, URISyntaxException {
        Optional<Movie> movieOptional = movieService.findById(id);

        if (movieOptional.isEmpty()) {
            throw new EntityNotFoundException("Movie not found");
        }

        return ImageHelper.getImage(movieOptional.get().getPosterUrl());
    }
}
