package com.example.nutriplan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(value = {"id"}, allowGetters = true, ignoreUnknown = true)
public record DishDto(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Schema(description = "Идентификатор блюда")
        Long id,

        @NotBlank
        @Schema(description = "Название блюда", example = "Салат Цезарь")
        @Size(min = 2, max = 16)
        String name,

        @Positive
        @Schema(description = "Калории на порцию", example = "250.5")
        double caloriesPerServing,

        @Positive
        @Schema(description = "Белки (г)", example = "12.3")
        double proteins,

        @Positive
        @Schema(description = "Жиры (г)", example = "8.5")
        double fats,

        @Positive
        @Schema(description = "Углеводы (г)", example = "20.0")
        double carbohydrates
) {
}

