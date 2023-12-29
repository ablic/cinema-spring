package com.ablic.cinema.dtos;

import com.ablic.cinema.models.Ticket;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Seat {
    private float priceMultiplier;
    private float leftSpace;
    private float rightSpace;

    @JsonIgnore
    private Ticket ticket;
}
