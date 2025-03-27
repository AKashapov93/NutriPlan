package com.example.nutriplan.dto;

import com.example.nutriplan.enums.MealType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record MealDto(
        @Schema(description = "Идентификатор пользователя")
        Long userId,
        @Schema(description = "Сумма калорий за прием пищи")
        double caloriesPerMeal,
        @Schema(description = "Тип приёма пищи", example = "BREAKFAST")
        MealType mealType,
        @Schema(description = "Время приема пищи")
        LocalDateTime createMealDate,
        @Schema(description = "Блюда составляющие этот прием пищи")
        List<DishDto> dishes
) {
}
