package com.example.nutriplan.controller;

import com.example.nutriplan.dto.DishDto;
import com.example.nutriplan.dto.DishesDto;
import com.example.nutriplan.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequestMapping("/dish")
@RestController
@Tag(name = "Блюдо")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @PostMapping
    @Operation(summary = "Добавление нового блюда")
    public ResponseEntity<DishDto> addDish(@Valid @RequestBody DishDto dishDto) {
        return ResponseEntity.ok(dishService.addDish(dishDto));
    }

    @GetMapping
    @Operation(summary = "Получение всех доступных блюд")
    public ResponseEntity<DishesDto> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }
}
