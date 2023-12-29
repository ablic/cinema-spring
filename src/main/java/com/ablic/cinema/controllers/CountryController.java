package com.ablic.cinema.controllers;

import com.ablic.cinema.models.Country;
import com.ablic.cinema.services.CountryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/countries")
public class CountryController {
    @Autowired
    private CountryService countryService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("countries", countryService.findAll());
        return "countries/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("country", new Country());
        return "countries/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Valid Country country,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            return "countries/create";
        }

        countryService.save(country);
        return "redirect:/countries";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        Optional<Country> countryOptional = countryService.findById(id);

        if (countryOptional.isEmpty()) {
            return "redirect:/countries";
        }

        model.addAttribute("country", countryOptional.get());
        return "countries/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute @Valid Country country,
                       BindingResult bindingResult,
                       Model model) {

        if (bindingResult.hasErrors()) {
            return "countries/edit";
        }

        countryService.save(country);
        return "redirect:/countries";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        countryService.tryDeleteById(id);
        return "redirect:/countries";
    }
}
