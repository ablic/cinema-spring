package com.ablic.cinema.services;

import com.ablic.cinema.models.Hall;
import com.ablic.cinema.repositories.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HallService {
    @Autowired
    private HallRepository hallRepository;

    public List<Hall> findAll() {
        return (List<Hall>) hallRepository.findAll();
    }

    public Optional<Hall> findById(Long id) {
        return hallRepository.findById(id);
    }

    public Hall save(Hall hall) {
        return hallRepository.save(hall);
    }

    public void delete(Hall hall) {
        hallRepository.delete(hall);
    }

}
