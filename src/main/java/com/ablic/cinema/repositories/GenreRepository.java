package com.ablic.cinema.repositories;

import com.ablic.cinema.models.Genre;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    Iterable<Genre> findByNameContainingIgnoreCase(String part);

    @Modifying
    @Query(value = "DELETE FROM movies_genres WHERE genre_id = :genreId", nativeQuery = true)
    void deleteFromMovieGenreByGenreId(Long genreId);
}
