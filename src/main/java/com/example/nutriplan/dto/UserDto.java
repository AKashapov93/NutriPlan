package com.example.nutriplan.dto;

import com.example.nutriplan.enums.Goal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;

@JsonIgnoreProperties(value = {"id", "dailyCalorieIntake"}, allowGetters = true)
public record UserDto(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Schema(description = "ID пользователя", example = "1")
        Long id,

        @Size(min = 4, max = 32)
        @Schema(description = "Логин пользователя", minLength = 4, maxLength = 32)
        String username,

        @Email
        @Schema(description = "Электронная почта", example = "user@example.com")
        String email,

        @Positive
        @Schema(description = "Возраст", example = "25")
        int age,

        @Positive
        @Schema(description = "Вес (кг)", example = "70.5")
        double weight,

        @Positive
        @Schema(description = "Рост (см)", example = "175.0")
        double height,

        @NotNull
        @Schema(description = "Цель", example = "WEIGHT_LOSS")
        Goal goal,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Schema(description = "Дневная норма калорий")
        double dailyCalorieIntake
) {
}
