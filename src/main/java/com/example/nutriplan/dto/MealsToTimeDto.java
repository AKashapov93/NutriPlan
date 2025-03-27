package com.example.nutriplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MealsToTimeDto(
        @Schema(description = "Список приемов пищи за день")
        List<MealDto> mealDtoList,
        @Schema(description = "Сумма калорий за день")
        double calories
) {
}
