package com.ablic.cinema.controllers;

import com.ablic.cinema.models.Genre;
import com.ablic.cinema.services.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/genres")
public class GenreController {
    @Autowired
    private GenreService genreService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("genres", genreService.findAll());
        return "genres/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("genre", new Genre());
        return "genres/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Valid Genre genre,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            return "genres/create";
        }

        genreService.save(genre);
        return "redirect:/genres";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        Optional<Genre> genreOptional = genreService.findById(id);

        if (genreOptional.isEmpty()) {
            return "redirect:/genres";
        }

        model.addAttribute("genre", genreOptional.get());
        return "genres/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute @Valid Genre genre,
                       BindingResult bindingResult,
                       Model model) {

        if (bindingResult.hasErrors()) {
            return "genres/edit";
        }

        genreService.save(genre);
        return "redirect:/genres";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        genreService.tryDeleteById(id);
        return "redirect:/genres";
    }
}
