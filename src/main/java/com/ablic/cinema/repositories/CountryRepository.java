package com.ablic.cinema.repositories;

import com.ablic.cinema.models.Country;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Long> {
    Iterable<Country> findByShortNameContainingIgnoreCaseOrFullNameContainingIgnoreCase(
            String shortNamePart,
            String fullNamePart);

    @Modifying
    @Query(value = "DELETE FROM movies_countries WHERE country_id = :countryId", nativeQuery = true)
    void deleteFromMovieCountryByCountryId(Long countryId);
}
