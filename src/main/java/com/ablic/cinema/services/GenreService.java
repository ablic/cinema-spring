package com.ablic.cinema.services;

import com.ablic.cinema.models.Genre;
import com.ablic.cinema.repositories.GenreRepository;
import com.ablic.cinema.repositories.MovieRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private MovieRepository movieRepository;

    public boolean existsById(Long id) {
        return genreRepository.existsById(id);
    }

    public List<Genre> findAll() {
        return findAll(null);
    }

    public List<Genre> findAll(@Nullable String namePart) {
        if (namePart == null) {
            return (List<Genre>)genreRepository.findAll();
        }
        return (List<Genre>)genreRepository.findByNameContainingIgnoreCase(namePart);
    }

    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }

    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Transactional
    public void tryDeleteById(Long id) {
        genreRepository.deleteFromMovieGenreByGenreId(id);
        genreRepository.deleteById(id);
    }
}
