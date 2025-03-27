package com.example.nutriplan.service;

import com.example.nutriplan.dto.MealsDto;
import com.example.nutriplan.dto.MealsToTimeDto;
import com.example.nutriplan.dto.TimeDto;
import com.example.nutriplan.dto.UserDto;
import com.example.nutriplan.enums.Goal;

public interface UserService {

    UserDto createUser(UserDto userDto);

    double calculateBMR(double weight, double height, int age, Goal goal);

    MealsDto getHistoryToDays(Long userId);

    Boolean calculateCaloriesPerDay(TimeDto timeDto);

    MealsToTimeDto getDailyCaloriesAndMeals(TimeDto timeDto);
}
