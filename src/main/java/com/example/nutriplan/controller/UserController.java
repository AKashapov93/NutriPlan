package com.example.nutriplan.controller;

import com.example.nutriplan.dto.MealsDto;
import com.example.nutriplan.dto.MealsToTimeDto;
import com.example.nutriplan.dto.TimeDto;
import com.example.nutriplan.dto.UserDto;
import com.example.nutriplan.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/user")
@RestController
@Tag(name = "Пользователь")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Создание пользователя")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @PostMapping("/history-to-day")
    @Operation(summary = "Получение суммы калорий и истории приемов пищи за день")
    public ResponseEntity<MealsToTimeDto> getDailyCaloriesAndMeals(@Valid @RequestBody TimeDto timeDto) {
        return ResponseEntity.ok(userService.getDailyCaloriesAndMeals(timeDto));
    }

    @PostMapping("/quota-to-day")
    @Operation(summary = "Проверка уложился ли пользователь в дневную норму")
    public ResponseEntity<Boolean> calculateCaloriesPerDay(@Valid @RequestBody TimeDto timeDto) {
        return ResponseEntity.ok(userService.calculateCaloriesPerDay(timeDto));
    }

    @GetMapping("/history/{id}")
    @Operation(summary = "Получение истории приемов пищи пользователя")
    public ResponseEntity<MealsDto> getHistoryToDays(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getHistoryToDays(id));
    }
}
