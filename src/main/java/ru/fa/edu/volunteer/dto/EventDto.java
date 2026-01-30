package ru.fa.edu.volunteer.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventDto {

    @NotBlank(message = "Название обязательно")
    @Size(max = 255)
    private String title;

    private String description;

    @NotNull(message = "Дата начала обязательна")
    private LocalDate startingDate;

    @NotNull(message = "Дата окончания обязательна")
    private LocalDate endingDate;

    @Size(max = 255)
    private String location;
}
