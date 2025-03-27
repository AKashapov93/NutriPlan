package com.example.nutriplan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Positive;
import java.time.LocalDate;

public record TimeDto(
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        @Schema(description = "Дата")
        LocalDate time,
        @Schema(description = "Идентификатор пользователя")
        @Positive
        Long userId
) {
}
