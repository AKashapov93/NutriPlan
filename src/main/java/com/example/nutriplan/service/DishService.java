package com.example.nutriplan.service;

import com.example.nutriplan.dto.DishDto;
import com.example.nutriplan.dto.DishesDto;

public interface DishService {
    DishDto addDish(DishDto dishDto);

    DishesDto getAllDishes();
}
