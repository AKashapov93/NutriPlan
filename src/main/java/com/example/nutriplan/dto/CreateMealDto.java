package com.example.nutriplan.dto;

import com.example.nutriplan.enums.MealType;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public record CreateMealDto(
        @NotNull
        @Positive
        @Schema(description = "Идентификатор пользователя")
        Long userId,

        @NotBlank
        @Schema(description = "Тип приёма пищи", example = "BREAKFAST")
        MealType mealType,

        @NotEmpty
        @Schema(description = "Список идентификаторов блюд")
        List<@NotNull @Positive Long> dishesId
) {
}
