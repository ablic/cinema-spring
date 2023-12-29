package com.ablic.cinema.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SessionDay {
    private String title;
    private LocalDate date;
}
