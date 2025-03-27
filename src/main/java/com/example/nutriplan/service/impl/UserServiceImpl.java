package com.example.nutriplan.service.impl;

import com.example.nutriplan.dto.*;
import com.example.nutriplan.entity.Meal;
import com.example.nutriplan.entity.User;
import com.example.nutriplan.enums.Goal;
import com.example.nutriplan.exeption.UserNotFoundException;
import com.example.nutriplan.mapper.MealMapper;
import com.example.nutriplan.mapper.UserMapper;
import com.example.nutriplan.repository.MealRepository;
import com.example.nutriplan.repository.UserRepository;
import com.example.nutriplan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    @Override
    public UserDto createUser(final UserDto userDto) {
        User user = userMapper.createUserDtoToUser(userDto);
        user.setDailyCalorieIntake(calculateBMR(user.getWeight(), user.getHeight(), user.getAge(), user.getGoal()));
        var createdUser = userRepository.save(user);
        return userMapper.userToUserDto(createdUser);
    }

    @Override
    public MealsDto getHistoryToDays(final Long userId) {
        if (!(userRepository.existsById(userId))) {
            throw new UserNotFoundException();
        }
        List<Meal> meals = mealRepository.findMealsByUserId(userId);
        Map<LocalDate, List<MealDto>> result = meals.stream().map(mealMapper::mealToMealDto)
                .collect(Collectors.groupingBy(meal -> meal.createMealDate().toLocalDate()));
        return new MealsDto(result);
    }

    @Override
    public Boolean calculateCaloriesPerDay(final TimeDto timeDto) {
        User user = userRepository.findById(timeDto.userId()).orElseThrow(UserNotFoundException::new);
        LocalDateTime startOfDay = timeDto.time().atStartOfDay();
        LocalDateTime endOfDay = timeDto.time().atTime(23, 59, 59, 999999);
        double totalCalories = mealRepository.calculateTotalCaloriesForDay(timeDto.userId(), startOfDay, endOfDay);
        return totalCalories <= user.getDailyCalorieIntake();
    }

    @Override
    public MealsToTimeDto getDailyCaloriesAndMeals(final TimeDto timeDto) {
        if (!(userRepository.existsById(timeDto.userId()))) {
            throw new UserNotFoundException();
        }
        LocalDateTime startOfDay = timeDto.time().atStartOfDay();
        LocalDateTime endOfDay = timeDto.time().atTime(23, 59, 59, 999999);
        List<Meal> mealsForDay = mealRepository.findMealsForUserOnDate(timeDto.userId(), startOfDay, endOfDay);
        double totalCalories = mealsForDay.stream().mapToDouble(Meal::getCaloriesPerMeal).sum();
        List<MealDto> meals = mealsForDay.stream().map(mealMapper::mealToMealDto).toList();
        return mealMapper.mapMealsToMealsToTimeDto(meals, totalCalories);
    }

    @Override
    public double calculateBMR(double weight, double height, int age, Goal goal) {
        return (10 * weight + 6.25 * height - 5 * age + (goal == Goal.WEIGHT_LOSS ? -161 : 5)) * 1.375;
    }
}
