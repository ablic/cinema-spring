package com.ablic.cinema.dtos;

import com.ablic.cinema.models.Ticket;
import com.ablic.cinema.validationgroups.HallEmailValidationGroup;
import com.ablic.cinema.validationgroups.HallSeatSelectionValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Order {

    @NotEmpty(message = "Выберите хотя бы одно место в зале", groups = HallSeatSelectionValidationGroup.class)
    private List<Ticket> tickets;

    @NotBlank(message = "Введите адрес электронной почты", groups = HallEmailValidationGroup.class)
    @Email(message = "Укажите корректное значение электронной почты", groups = HallEmailValidationGroup.class)
    private String email;

    private String movieName;
    private LocalDateTime sessionDateTime;
    private int hallNumber;
}
