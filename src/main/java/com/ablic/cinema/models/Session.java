package com.ablic.cinema.models;

import com.ablic.cinema.validationgroups.SessionBaseValidationGroup;
import com.ablic.cinema.validationgroups.SessionMovieValidationGroup;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sessions")
@NoArgsConstructor
@Data
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonManagedReference
    @NotNull(message = "Укажите фильм", groups = SessionMovieValidationGroup.class)
    private Movie movie;

    @NotNull(message = "Укажите дату и время", groups = SessionBaseValidationGroup.class)
    @FutureOrPresent(message = "Дата и время проведения сеанса не может быть в прошлом", groups = SessionBaseValidationGroup.class)
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    @NotNull(message = "Выберите зал", groups = SessionBaseValidationGroup.class)
    private Hall hall;

    @Min(value = 0, message = "Цена не может быть отрицательной", groups = SessionBaseValidationGroup.class)
    private float minPrice;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Ticket> tickets;

    @Transient
    private int numberAvailableSeats;
}
