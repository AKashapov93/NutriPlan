package com.example.nutriplan.controller;

import com.example.nutriplan.dto.CreateMealDto;
import com.example.nutriplan.dto.MealDto;
import com.example.nutriplan.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/meal")
@RestController
@Tag(name = "Прием пищи")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @PostMapping
    @Operation(summary = "Добавление приема пищи")
    public ResponseEntity<MealDto> addMeal(@RequestBody CreateMealDto createMealDto) {
        return ResponseEntity.ok(mealService.addMeal(createMealDto));
    }


}
