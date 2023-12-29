package com.ablic.cinema.services;

import com.ablic.cinema.helpers.ImageHelper;
import com.ablic.cinema.models.Movie;
import com.ablic.cinema.repositories.MovieRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public boolean existsById(Long id) {
        return movieRepository.existsById(id);
    }

    public List<Movie> findAll() {
        return findAll(null);
    }

    public List<Movie> findAll(@Nullable String namePart) {
        if (namePart == null) {
            return (List<Movie>) movieRepository.findAll();
        }
        return (List<Movie>) movieRepository.findByNameContainingIgnoreCase(namePart);
    }

    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    public void delete(Movie movie) {
        movieRepository.delete(movie);
    }
    public void deleteById(Long id) {
        movieRepository.deleteById(id);
    }

    public void updateMoviePoster(Movie movie, MultipartFile poster) throws URISyntaxException, IOException {
        if (poster.isEmpty()) {
            if (movie.getPosterUrl() != null) {
                ImageHelper.deleteImage(movie.getPosterUrl());
            }
        }
        else {
            String fileName = ImageHelper.generateUniqName() + ".jpeg";
            ImageHelper.loadImage(fileName, "/posters/", poster);
            movie.setPosterUrl("/posters/" + fileName);
        }
    }
}
