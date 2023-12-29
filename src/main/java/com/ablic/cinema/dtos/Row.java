package com.ablic.cinema.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Row {
    private List<Seat> seats;
    private float frontSpace;
    private float backSpace;
}
