package com.ablic.cinema.repositories;

import com.ablic.cinema.models.AgeLimit;
import org.springframework.data.repository.CrudRepository;

public interface AgeLimitRepository extends CrudRepository<AgeLimit, Long> {
}
