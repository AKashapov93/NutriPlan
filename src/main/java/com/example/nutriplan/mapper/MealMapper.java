package com.example.nutriplan.mapper;

import com.example.nutriplan.dto.CreateMealDto;
import com.example.nutriplan.dto.MealDto;
import com.example.nutriplan.dto.MealsToTimeDto;
import com.example.nutriplan.entity.Meal;
import com.example.nutriplan.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DishMapper.class})
public interface MealMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "caloriesPerMeal", ignore = true)
    @Mapping(target = "createMealDate", expression = "java(java.time.LocalDateTime.now())")
    Meal createMealDtoToMeal(CreateMealDto createMealDto, User user);

    @Mapping(target = "userId", source = "user.id")
    MealDto mealToMealDto(Meal meal);

    @Mapping(target = "mealDtoList", source = "meals")
    @Mapping(target = "calories", source = "totalCalories")
    MealsToTimeDto mapMealsToMealsToTimeDto(List<MealDto> meals, double totalCalories);


}

