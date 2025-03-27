package com.example.nutriplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record MealsDto(
        @Schema(description = "Сгруппированные по дням приемы пищи")
        Map<LocalDate, List<MealDto>> localDateTimeListMap
) {
}
