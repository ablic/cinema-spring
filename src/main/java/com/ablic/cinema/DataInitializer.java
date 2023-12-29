package com.ablic.cinema;

import com.ablic.cinema.dtos.Row;
import com.ablic.cinema.dtos.Seat;
import com.ablic.cinema.models.Hall;
import com.ablic.cinema.models.Movie;
import com.ablic.cinema.models.Session;
import com.ablic.cinema.services.HallService;
import com.ablic.cinema.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private HallService hallService;

    @Autowired
    private SessionService sessionService;

    @Override
    public void run(String... args) throws Exception {
        initHalls();
        initSessions();
    }

    private void initHalls() {
        List<Row> rows1 = new ArrayList<Row>();

        Row row = new Row();
        Seat seat = new Seat();
        seat.setPriceMultiplier(1f);

        List<Seat> seats = new ArrayList<>();
        seats.add(seat);
        seats.add(seat);
        seats.add(seat);

        row.setSeats(seats);
        rows1.add(row);
        rows1.add(row);
        rows1.add(row);

        Hall hall1 = new Hall();
        hall1.setRows(rows1);
        hall1.setNumber(1);

        hallService.save(hall1);
    }

    private void initSessions() {
        Session session = new Session();

        Movie movie = new Movie(); movie.setId(1L);

        session.setMovie(movie);
        session.setHall(hallService.findById(1L).get());
        session.setDateTime(LocalDateTime.now());
        session.setMinPrice(300);

        sessionService.save(session);
    }
}
