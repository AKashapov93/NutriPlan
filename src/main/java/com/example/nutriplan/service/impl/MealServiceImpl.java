package com.example.nutriplan.service.impl;

import com.example.nutriplan.dto.CreateMealDto;
import com.example.nutriplan.dto.MealDto;
import com.example.nutriplan.entity.Dish;
import com.example.nutriplan.entity.Meal;
import com.example.nutriplan.entity.User;
import com.example.nutriplan.exeption.DishNotFoundException;
import com.example.nutriplan.exeption.UserNotFoundException;
import com.example.nutriplan.mapper.MealMapper;
import com.example.nutriplan.repository.DishRepository;
import com.example.nutriplan.repository.MealRepository;
import com.example.nutriplan.repository.UserRepository;
import com.example.nutriplan.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final MealMapper mealMapper;
    private final DishRepository dishRepository;

    @Override
    public MealDto addMeal(CreateMealDto createMealDto) {
        User user = userRepository.findById(createMealDto.userId()).orElseThrow(UserNotFoundException::new);
        Meal meal = mealMapper.createMealDtoToMeal(createMealDto, user);
        List<Dish> dishList = createMealDto.dishesId().stream()
                .map(dishRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (dishList.size() != createMealDto.dishesId().size()) {
            List<Long> missingDishIds = createMealDto.dishesId().stream()
                    .filter(id -> dishList.stream().noneMatch(dish -> dish.getId().equals(id)))
                    .toList();

            throw new DishNotFoundException("Данные блюда не найдены: " + missingDishIds);
        }
        double caloriesPerMeal = dishList.stream().map(Dish::getCaloriesPerServing)
                .reduce(0.0, Double::sum);

        meal.setCaloriesPerMeal(caloriesPerMeal);
        meal.setDishes(dishList);
        return mealMapper.mealToMealDto(mealRepository.save(meal));
    }
}
