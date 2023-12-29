package com.ablic.cinema.services;

import com.ablic.cinema.dtos.*;
import com.ablic.cinema.models.Session;
import com.ablic.cinema.models.Ticket;
import com.ablic.cinema.repositories.SessionRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private EmailService emailService;

    public List<Session> findAllByMovieId(@Nonnull Long movieId) {
        List<Session> sessions = (List<Session>)sessionRepository.findAllByMovieId(movieId);

        for (Session session : sessions) {
            session.setNumberAvailableSeats(ticketService.getNumberAvailableSessionSeats(session.getId()));
        }

        return sessions;
    }

    public List<Session> findAllByDate(LocalDate date) {

        LocalDateTime dateTimeFrom = date == null
                ? LocalDateTime.now()
                : date.atStartOfDay();

        LocalDateTime dateTimeTo = date.atStartOfDay().plusDays(1);

        return (List<Session>)sessionRepository.findByDateTimeBetween(dateTimeFrom, dateTimeTo);
    }

    public List<SessionDay> getPaginationDays() {

        List<LocalDate> dates = (List<LocalDate>) sessionRepository.findDistinctSessionDates();
        List<SessionDay> paginationDays = new ArrayList<>(dates.size());

        for (LocalDate date : dates) {
            SessionDay pDay = new SessionDay();
            pDay.setDate(date);

            if (date.equals(LocalDate.now())) {
                pDay.setTitle("Сегодня");
            }
            else if (date.equals(LocalDate.now().plusDays(1))) {
                pDay.setTitle("Завтра");
            }
            else {
                pDay.setTitle(getDayOfWeek(date) + " " + date.format(DateTimeFormatter.ofPattern("dd.MM")));
            }

            paginationDays.add(pDay);
        }

        return paginationDays;
        //return null;
    }

    public Iterable<Session> findAll() {
        return sessionRepository.findAll();
    }

    public Optional<Session> findById(Long id) {
        return sessionRepository.findById(id);
    }

    public Session save(Session session) {

        Session savedSession = sessionRepository.save(session);

        List<Ticket> tickets = new ArrayList<>();
        List<Row> rows = session.getHall().getRows();
        for (int i = 1; i <= rows.size(); i++) {

            List<Seat> seats = rows.get(i - 1).getSeats();
            for (int j = 1; j <= seats.size(); j++) {

                Ticket ticket = new Ticket();

                ticket.setSession(savedSession);
                ticket.setRowNumber(i);
                ticket.setSeatNumber(j);
                ticket.setPrice(session.getMinPrice() * seats.get(j - 1).getPriceMultiplier());

                tickets.add(ticket);
            }
        }

        ticketService.saveAll(tickets);

        return savedSession;
    }

    public void delete(Session session) {
        sessionRepository.delete(session);
    }


    public void confirmOrder(Order order) {

        for (Ticket ticket : order.getTickets()) {
            ticket.setOwnerEmail(order.getEmail());
        }

        ticketService.saveAll(order.getTickets());
        sendEmail(order);
    }

    public void confirmOrder(OrderRest orderRest) {

        List<Ticket> tickets = ticketService.findAllByIdIn(orderRest.getTicketIds());

        for (Ticket ticket : tickets) {
            ticket.setOwnerEmail(orderRest.getEmail());
        }

        ticketService.saveAll(tickets);

        Order order = new Order();

        Session session = tickets.get(0).getSession();

        order.setHallNumber(session.getHall().getNumber());
        order.setMovieName(session.getMovie().getName());
        order.setTickets(tickets);
        order.setEmail(orderRest.getEmail());
        order.setSessionDateTime(session.getDateTime());

        sendEmail(order);
    }

    private String getDayOfWeek(LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case MONDAY -> "Понедельник";
            case TUESDAY -> "Вторник";
            case WEDNESDAY -> "Среда";
            case THURSDAY -> "Четверг";
            case FRIDAY -> "Пятница";
            case SATURDAY -> "Суббота";
            case SUNDAY -> "Воскресенье";
            default -> "";
        };
    }

    private void sendEmail(Order order) {

        String subject = "Покупка билета на сеанс фильма " + order.getMovieName();
        StringBuilder message = new StringBuilder();

        message
                .append("Вы приобрели билет(ы) на сеанс фильма ")
                .append(order.getMovieName())
                .append(".\nДата и время проведения: ")
                .append(order.getSessionDateTime().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")))
                .append(".\nПриобретенные билеты:\n");

        for (Ticket ticket : order.getTickets()) {
            message
                    .append("- билет №")
                    .append(ticket.getId())
                    .append(", ряд ")
                    .append(ticket.getRowNumber())
                    .append(", место ")
                    .append(ticket.getSeatNumber())
                    .append(";\n");
        }

        emailService.sendSimpleEmail(order.getEmail(), subject, message.toString());
    }
}
