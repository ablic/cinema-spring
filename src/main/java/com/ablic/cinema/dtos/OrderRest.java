package com.ablic.cinema.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderRest {
    private String email;
    private List<Long> ticketIds;
}
