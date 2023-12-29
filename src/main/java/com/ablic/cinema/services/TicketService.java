package com.ablic.cinema.services;

import com.ablic.cinema.models.Ticket;
import com.ablic.cinema.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    public int getNumberAvailableSessionSeats(Long sessionId) {
        return ticketRepository.countBySessionIdAndOwnerEmailIsNull(sessionId);
    }

    public void saveAll(List<Ticket> tickets) {
        ticketRepository.saveAll(tickets);
    }

    public List<Ticket> findAllByIdIn(List<Long> ids) {
        return (List<Ticket>) ticketRepository.findAllByIdIn(ids);
    }
}
