package com.ablic.cinema.repositories;

import com.ablic.cinema.models.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SessionRepository extends CrudRepository<Session, Long> {
    Iterable<Session> findByDateTimeBetween(LocalDateTime from, LocalDateTime to);
    Iterable<Session> findAllByMovieId(Long movieId);
    @Query("SELECT DISTINCT CAST(s.dateTime AS LocalDate) FROM Session s")
    Iterable<LocalDate> findDistinctSessionDates();
}
