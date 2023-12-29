package com.ablic.cinema.controllers;

import com.ablic.cinema.dtos.Order;
import com.ablic.cinema.dtos.Row;
import com.ablic.cinema.dtos.Seat;
import com.ablic.cinema.helpers.StringListBuilder;
import com.ablic.cinema.models.Session;
import com.ablic.cinema.models.Ticket;
import com.ablic.cinema.services.HallService;
import com.ablic.cinema.services.MovieService;
import com.ablic.cinema.services.SessionService;
import com.ablic.cinema.services.TicketService;
import com.ablic.cinema.validationgroups.HallEmailValidationGroup;
import com.ablic.cinema.validationgroups.HallSeatSelectionValidationGroup;
import com.ablic.cinema.validationgroups.SessionBaseValidationGroup;
import com.ablic.cinema.validationgroups.SessionMovieValidationGroup;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sessions")
public class SessionController {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private HallService hallService;
    @Autowired
    private TicketService ticketService;

    @GetMapping
    public String index() {
        return "redirect:/sessions/" + LocalDate.now();
    }

    @GetMapping("{date}")
    public String index(@PathVariable LocalDate date, Model model) {

        model.addAttribute("date", date);
        model.addAttribute("paginationDays", sessionService.getPaginationDays());
        model.addAttribute("sessions", sessionService.findAllByDate(date));

        return "sessions/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("sess", new Session());
        model.addAttribute("allHalls", hallService.findAll());
        return "sessions/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("sess") @Validated(SessionBaseValidationGroup.class) Session sessionBase,
                         BindingResult bindingResult,
                         Model model,
                         HttpSession httpSession) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allHalls", hallService.findAll());
            return "sessions/create";
        }

        httpSession.setAttribute("sessionBase", sessionBase);
        return "redirect:/sessions/selectMovie";
    }

    @GetMapping("/selectMovie")
    public String selectMovie(Model model,
                              @RequestParam(value = "filter", required = false) String filter) {

        model.addAttribute("sess", new Session());
        model.addAttribute("filteredMovies", movieService.findAll(filter));

        return "sessions/selectMovie";
    }

    @PostMapping("/selectMovie")
    public String selectMovie(@ModelAttribute("sess") @Validated(SessionMovieValidationGroup.class) Session sessionMovie,
                              BindingResult bindingResult,
                              Model model,
                              HttpSession httpSession) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("filteredMovies", movieService.findAll());
            return "sessions/selectMovie";
        }

        Session sessionBase = (Session) httpSession.getAttribute("sessionBase");
        httpSession.removeAttribute("sessionBase");

        sessionBase.setMovie(sessionMovie.getMovie());
        sessionService.save(sessionBase);

        return "redirect:/sessions";
    }

    @GetMapping("/{id}/selectSeats")
    public String selectSeats(@PathVariable Long id, Model model) {

        Optional<Session> session = sessionService.findById(id);

        if (session.isEmpty()) {
            return "redirect:/sessions";
        }

        model.addAttribute("rows", setTicketsToRows(session.get()));
        model.addAttribute("order", new Order());

        return "sessions/selectSeats";
    }

    @PostMapping("/{id}/selectSeats")
    public String selectSeats(@PathVariable Long id,
                              @ModelAttribute("order") @Validated(HallSeatSelectionValidationGroup.class) Order order,
                              BindingResult bindingResult,
                              Model model,
                              HttpSession httpSession) {

        Optional<Session> session = sessionService.findById(id);

        if (session.isEmpty()) {
            return "redirect:/sessions";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("rows", setTicketsToRows(session.get()));
            return "sessions/selectSeats";
        }

        for (Ticket ticket : order.getTickets()) {
            if (ticket.getOwnerEmail() != null) {
                bindingResult.addError(new FieldError("orderTickets", "tickets", "Одно из выбранных вами мест было куплено. Пожалуйста, выберите другое место"));
                model.addAttribute("rows", setTicketsToRows(session.get()));
                return "sessions/selectSeats";
            }
        }

        httpSession.setAttribute("orderTickets", order);
        return "redirect:/sessions/" + id + "/selectEmail";
    }

    @GetMapping("/{id}/selectEmail")
    public String selectEmail(@PathVariable Long id, Model model) {

        if (sessionService.findById(id).isEmpty()) {
            return "redirect:/sessions";
        }

        model.addAttribute("order", new Order());
        return "sessions/selectEmail";
    }

    @PostMapping("/{id}/selectEmail")
    public String selectEmail(@PathVariable Long id,
                              @ModelAttribute("order") @Validated(HallEmailValidationGroup.class) Order order,
                              BindingResult bindingResult,
                              HttpSession httpSession) {

        if (sessionService.findById(id).isEmpty()) {
            return "redirect:/sessions";
        }

        if (bindingResult.hasErrors()) {
            return "sessions/selectEmail";
        }

        httpSession.setAttribute("orderEmail", order);
        return "redirect:/sessions/" + id + "/confirm";
    }

    @GetMapping("/{id}/confirm")
    public String confirm(@PathVariable Long id,
                          Model model,
                          HttpSession httpSession) {

        Optional<Session> sessionOptional = sessionService.findById(id);

        if (sessionOptional.isEmpty()) {
            return "redirect:/sessions";
        }

        Session session = sessionOptional.get();

        Order order = new Order();
        Order orderTickets = (Order) httpSession.getAttribute("orderTickets");
        Order orderEmail = (Order) httpSession.getAttribute("orderEmail");

        order.setTickets(orderTickets.getTickets());
        order.setEmail(orderEmail.getEmail());
        order.setMovieName(session.getMovie().getName());
        order.setSessionDateTime(session.getDateTime());
        order.setHallNumber(session.getHall().getNumber());

        model.addAttribute("order", order);
        model.addAttribute("ticketsStr", StringListBuilder.get(
                orderTickets
                        .getTickets()
                        .stream()
                        .map(t -> t.getRowNumber() + "-" + t.getSeatNumber())
                        .collect(Collectors.toList()), ';'));

        model.addAttribute("totalPrice", order
                .getTickets()
                .stream()
                .mapToDouble(Ticket::getPrice)
                .sum());

        httpSession.setAttribute("order", order);

        return "sessions/confirm";
    }

    @PostMapping("/{id}/confirm")
    public String confirm(@PathVariable Long id, HttpSession httpSession) {

        Optional<Session> sessionOptional = sessionService.findById(id);

        if (sessionOptional.isEmpty()) {
            return "redirect:/sessions";
        }

        Order order = (Order) httpSession.getAttribute("order");

        for (Ticket ticket : order.getTickets()) {
            if (ticket.getOwnerEmail() != null) {
                return "redirect:/sessions/" + id + "/selectSeats";
            }
        }

        sessionService.confirmOrder(order);
        return "redirect:/sessions";
    }

    private List<Row> setTicketsToRows(Session session) {
        List<Row> rows = session.getHall().getRows();

        for (Ticket ticket : session.getTickets()) {

            Seat seat = rows
                    .get(ticket.getRowNumber() - 1)
                    .getSeats()
                    .get(ticket.getSeatNumber() - 1);

            seat.setTicket(ticket);
        }

        return rows;
    }
}
