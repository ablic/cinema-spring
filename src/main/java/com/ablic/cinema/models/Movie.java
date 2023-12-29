package com.ablic.cinema.models;

import com.ablic.cinema.helpers.StringListBuilder;
import com.ablic.cinema.validationgroups.MovieBaseValidationGroup;
import com.ablic.cinema.validationgroups.MovieCountryValidationGroup;
import com.ablic.cinema.validationgroups.MovieGenreValidationGroup;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "movies")
@NoArgsConstructor
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Поле обязятельно для заполнения", groups = MovieBaseValidationGroup.class)
    @Size(min=1, max=100, message = "Слишком короткое или длинное название", groups = MovieBaseValidationGroup.class)
    private String name;

    @Min(value = 1, message = "Минимальное значение - 1", groups = MovieBaseValidationGroup.class)
    @Max(value = 300, message = "Максимальное значение - 300", groups = MovieBaseValidationGroup.class)
    private int duration;

    @NotNull(message = "Укажите дату выхода в прокат", groups = MovieBaseValidationGroup.class)
    @PastOrPresent(message = "Дата выхода фильма в прокат не может быть в будущем", groups = MovieBaseValidationGroup.class)
    private LocalDate releaseDate;

    @ManyToOne
    @NotNull(message = "Укажите возрастное ограничение", groups = MovieBaseValidationGroup.class)
    private AgeLimit ageLimit;

    @Column(length = 1000)
    @Size(max=1000, message = "Описание не должно превышать 1000 символов", groups = MovieBaseValidationGroup.class)
    private String description;

    private String posterUrl;

    @ManyToMany
    @JoinTable(
            name = "movies_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonManagedReference
    @NotEmpty(message = "Необходим хотя бы один жанр", groups = MovieGenreValidationGroup.class)
    private List<Genre> genres;

    @ManyToMany
    @JoinTable(
            name = "movies_countries",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    @JsonManagedReference
    @NotEmpty(message = "Необходима хотя бы одна страна", groups = MovieCountryValidationGroup.class)
    private List<Country> countries;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Session> sessions;

    @JsonIgnore
    @Transient
    public String getGenresStr() {
        return StringListBuilder.get(genres.stream().map(Genre::getName).collect(Collectors.toList()), ',');
    }

    @JsonIgnore
    @Transient
    public String getCountriesStr() {
        return StringListBuilder.get(countries.stream().map(Country::getShortName).collect(Collectors.toList()), ',');
    }
}
