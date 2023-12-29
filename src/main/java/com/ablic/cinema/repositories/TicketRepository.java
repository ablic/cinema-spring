package com.ablic.cinema.repositories;

import com.ablic.cinema.models.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
    int countBySessionIdAndOwnerEmailIsNull(Long sessionId);
    Iterable<Ticket> findAllByIdIn(List<Long> ids);
}
