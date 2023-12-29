package com.ablic.cinema.services;

import com.ablic.cinema.models.Country;
import com.ablic.cinema.repositories.CountryRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;

    public List<Country> findAll() {
        return findAll(null);
    }

    public List<Country> findAll(@Nullable String namePart) {
        if (namePart == null) {
            return (List<Country>)countryRepository.findAll();
        }
        return (List<Country>)countryRepository.findByShortNameContainingIgnoreCaseOrFullNameContainingIgnoreCase(namePart, namePart);
    }

    public Optional<Country> findById(Long id) {
        return countryRepository.findById(id);
    }

    public Country save(Country country) {
        return countryRepository.save(country);
    }

    public void delete(Country country) {
        countryRepository.delete(country);
    }

    @Transactional
    public void tryDeleteById(Long id) {
        countryRepository.deleteFromMovieCountryByCountryId(id);
        countryRepository.deleteById(id);
    }
}
