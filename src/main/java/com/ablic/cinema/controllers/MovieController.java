package com.ablic.cinema.controllers;

import com.ablic.cinema.helpers.ImageHelper;
import com.ablic.cinema.models.Movie;
import com.ablic.cinema.services.*;
import com.ablic.cinema.validationgroups.MovieBaseValidationGroup;
import com.ablic.cinema.validationgroups.MovieCountryValidationGroup;
import com.ablic.cinema.validationgroups.MovieGenreValidationGroup;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private AgeLimitService ageLimitService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("movies", movieService.findAll());
        return "movies/index";
    }

    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable("id") Long id) {
        Optional<Movie> movieOptional = movieService.findById(id);

        if (movieOptional.isEmpty()) {
            return "redirect:/movies";
        }

        Movie movie = movieOptional.get();

        model.addAttribute("movie", movie);
        model.addAttribute("sessions", sessionService.findAllByMovieId(id));
        model.addAttribute("genresStr", movie.getGenresStr());
        model.addAttribute("countriesStr", movie.getCountriesStr());

        return "movies/details";
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("movie", new Movie());
        model.addAttribute("allAgeLimits", ageLimitService.findAll());

        return "movies/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Validated(MovieBaseValidationGroup.class) Movie movie,
                         BindingResult bindingResult,
                         @RequestPart MultipartFile poster,
                         Model model,
                         HttpSession httpSession) throws URISyntaxException, IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allAgeLimits", ageLimitService.findAll());
            return "movies/create";
        }

        movieService.updateMoviePoster(movie, poster);
        httpSession.setAttribute("movieBase", movie);
        return "redirect:/movies/selectGenres";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        Optional<Movie> movieOptional = movieService.findById(id);

        if (movieOptional.isEmpty()) {
            return "redirect:/movies";
        }

        model.addAttribute("movie", movieOptional.get());
        model.addAttribute("allAgeLimits", ageLimitService.findAll());

        return "movies/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute @Validated(MovieBaseValidationGroup.class) Movie movie,
                         BindingResult bindingResult,
                         @RequestPart MultipartFile poster,
                         Model model,
                         HttpSession httpSession) throws URISyntaxException, IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allAgeLimits", ageLimitService.findAll());
            return "movies/edit";
        }

        if (movie.getPosterUrl() != null) {
            ImageHelper.deleteImage(movie.getPosterUrl());
        }

        movieService.updateMoviePoster(movie, poster);
        httpSession.setAttribute("movieBase", movie);
        return "redirect:/movies/selectGenres/" + movie.getId();
    }

    @GetMapping("/selectGenres")
    public String selectGenres(Model model,
                               @RequestParam(value = "filter", required = false) String filter) {

        Movie movie = new Movie();
        movie.setGenres(new ArrayList<>());

        model.addAttribute("movie",movie);
        model.addAttribute("allGenres", genreService.findAll(filter));
        return "movies/selectGenres";
    }

    @GetMapping("/selectGenres/{id}")
    public String selectGenres(@PathVariable Long id,
                               Model model,
                               @RequestParam(value = "filter", required = false) String filter) {

        Optional<Movie> movieOptional = movieService.findById(id);
        movieOptional.ifPresent(movie -> model.addAttribute("movie", movie));
        model.addAttribute("allGenres", genreService.findAll(filter));
        return "movies/selectGenres";
    }

    @PostMapping("/selectGenres")
    public String selectGenres(@ModelAttribute @Validated(MovieGenreValidationGroup.class) Movie movie,
                               BindingResult bindingResult,
                               Model model,
                               HttpSession httpSession) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allGenres", genreService.findAll());
            return "movies/selectGenres";
        }

        httpSession.setAttribute("movieGenres", movie);

        return "redirect:/movies/selectCountries";
    }

    @PostMapping("/selectGenres/{id}")
    public String selectGenres(@PathVariable(required = false) Long id,
                               @ModelAttribute @Validated(MovieGenreValidationGroup.class) Movie movie,
                               BindingResult bindingResult,
                               Model model,
                               HttpSession httpSession) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allGenres", genreService.findAll());
            return "movies/selectGenres";
        }

        httpSession.setAttribute("movieGenres", movie);

        return "redirect:/movies/selectCountries/" + id;

    }

    @GetMapping("/selectCountries")
    public String selectCountries(Model model,
                                  @RequestParam(value = "filter", required = false) String filter) {

        Movie movie = new Movie();
        movie.setCountries(new ArrayList<>());

        model.addAttribute("movie", movie);
        model.addAttribute("allCountries", countryService.findAll(filter));
        return "movies/selectCountries";
    }

    @GetMapping("/selectCountries/{id}")
    public String selectCountries(@PathVariable Long id,
                                  Model model,
                                  @RequestParam(value = "filter", required = false) String filter) {

        Optional<Movie> movieOptional = movieService.findById(id);
        movieOptional.ifPresent(movie -> model.addAttribute("movie", movie));
        model.addAttribute("allCountries", countryService.findAll(filter));
        return "movies/selectCountries";
    }

    @PostMapping("/selectCountries")
    public String selectCountries(@ModelAttribute("movie") @Validated(MovieCountryValidationGroup.class) Movie movieCountries,
                                  BindingResult bindingResult,
                                  Model model,
                                  HttpSession httpSession) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allCountries", countryService.findAll());
            return "movies/selectCountries";
        }

        Movie movieBase = (Movie) httpSession.getAttribute("movieBase");
        Movie movieGenres = (Movie) httpSession.getAttribute("movieGenres");

        httpSession.removeAttribute("movieBase");
        httpSession.removeAttribute("movieGenres");

        movieBase.setGenres(movieGenres.getGenres());
        movieBase.setCountries(movieCountries.getCountries());

        movieService.save(movieBase);

        return "redirect:/movies";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable("id") Long id) {
        Optional<Movie> optionalMovie = movieService.findById(id);

        if (optionalMovie.isEmpty()) {
            return "redirect:/movies";
        }

        model.addAttribute("movie", optionalMovie.get());
        return "movies/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) throws URISyntaxException {

        Optional<Movie> movieOptional = movieService.findById(id);

        if (movieOptional.isPresent()) {

            Movie movie = movieOptional.get();

            if (movie.getPosterUrl() != null) {
                ImageHelper.deleteImage(movie.getPosterUrl());
            }

            movieService.delete(movie);
        }

        return "redirect:/movies";
    }
}
