package com.example.nutriplan.service;

import com.example.nutriplan.dto.CreateMealDto;
import com.example.nutriplan.dto.MealDto;

public interface MealService {

    MealDto addMeal(CreateMealDto createMealDto);
}
