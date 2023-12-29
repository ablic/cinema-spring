package com.ablic.cinema.controllers.api;

import com.ablic.cinema.dtos.OrderRest;
import com.ablic.cinema.dtos.SessionDay;
import com.ablic.cinema.models.Session;
import com.ablic.cinema.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionRestController {
    @Autowired
    private SessionService sessionService;

    @GetMapping("/days")
    public List<SessionDay> getSessionDays() {
        return sessionService.getPaginationDays();
    }

    @GetMapping("/allByDate/{date}")
    public List<Session> getAllByDate(@PathVariable LocalDate date) {
        return sessionService.findAllByDate(date);
    }

    @GetMapping("/allByMovie/{date}")
    public List<Session> getAllByMovieId(@PathVariable Long id) {
        return sessionService.findAllByMovieId(id);
    }

    @GetMapping("{id}")
    public Session getById(@PathVariable Long id) {
        return sessionService.findById(id).orElse(null);
    }

    @PostMapping("/order")
    public void order(@RequestBody OrderRest orderRest) {
        sessionService.confirmOrder(orderRest);
    }
}
