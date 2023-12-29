package com.ablic.cinema.services;

import com.ablic.cinema.models.AgeLimit;
import com.ablic.cinema.repositories.AgeLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgeLimitService {
    @Autowired
    private AgeLimitRepository ageLimitRepository;

    public List<AgeLimit> findAll() {
        return (List<AgeLimit>) ageLimitRepository.findAll();
    }
}
