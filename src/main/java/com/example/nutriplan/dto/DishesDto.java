package com.example.nutriplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record DishesDto(
        @Schema(description = "Список блюд.")
        List<DishDto> dishes
) {
}
