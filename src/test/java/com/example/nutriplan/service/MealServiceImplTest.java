package com.example.nutriplan.service;

import com.example.nutriplan.dto.CreateMealDto;
import com.example.nutriplan.dto.DishDto;
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
import com.example.nutriplan.service.impl.MealServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.nutriplan.enums.Goal.MAINTENANCE;
import static com.example.nutriplan.enums.MealType.LUNCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MealServiceImplTest {

    @Mock
    private MealRepository mealRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MealMapper mealMapper;
    @Mock
    private DishRepository dishRepository;
    @InjectMocks
    private MealServiceImpl mealServiceImpl;

    private Dish dish;
    private CreateMealDto createMealDto;
    private User user;
    private Meal meal;
    private MealDto mealDto;
    private List<DishDto> dishDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("asd@gmail.com");
        user.setHeight(170);
        user.setWeight(60);
        user.setGoal(MAINTENANCE);
        user.setDailyCalorieIntake(2000);

        List<Long> dishes = new ArrayList<>();
        dishes.add(1L);
        createMealDto = new CreateMealDto(1L, LUNCH, dishes);
        dish = new Dish();
        dish.setId(1L);
        dish.setName("Salad");
        dish.setCaloriesPerServing(150.0);
        dish.setProteins(5.0);
        dish.setFats(10.0);
        dish.setCarbohydrates(15.0);
        dishDto = Collections.singletonList(new DishDto(1L, "Salad", 150.0, 5.0, 10.0, 15.0));

        meal = new Meal();
        meal.setId(1L);
        meal.setDishes(List.of(dish));
        meal.setUser(user);
        meal.setMealType(LUNCH);
        meal.setCaloriesPerMeal(150.0);
        meal.setCreateMealDate(LocalDateTime.now());

        mealDto = new MealDto(1L, 150.0, LUNCH, LocalDateTime.now(), dishDto);
    }

    @Test
    @DisplayName("Успешное добавление приема пищи")
    public void givenCreateMealDto_whenAddMeal_thenSaveMealAndReturnMealDto() {
        when(userRepository.findById(createMealDto.userId())).thenReturn(Optional.ofNullable(user));
        when(mealMapper.createMealDtoToMeal(createMealDto, user)).thenReturn(meal);
        when(dishRepository.findById(1L)).thenReturn(Optional.ofNullable(dish));
        when(mealRepository.save(meal)).thenReturn(meal);
        when(mealMapper.mealToMealDto(meal)).thenReturn(mealDto);

        MealDto result = mealServiceImpl.addMeal(createMealDto);

        assertNotNull(result);
        assertEquals(mealDto, result);

        InOrder inOrder = inOrder(userRepository, mealMapper, dishRepository, mealRepository, mealMapper);

        inOrder.verify(userRepository, times(1)).findById(1L);
        inOrder.verify(mealMapper, times(1)).createMealDtoToMeal(createMealDto, user);
        inOrder.verify(dishRepository, times(1)).findById(anyLong());
        inOrder.verify(mealRepository, times(1)).save(meal);
        inOrder.verify(mealMapper, times(1)).mealToMealDto(meal);

    }

    @Test
    @DisplayName("Должно выбросить исключение, если пользователь не найден")
    void givenCreateMealDto_whenUserNotFound_thenThrowUserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> mealServiceImpl.addMeal(createMealDto));
        verify(userRepository).findById(1L);
        verifyNoInteractions(dishRepository, mealRepository, mealMapper);
    }

    @Test
    @DisplayName("Должно выбросить исключение, если некоторые блюда не найдены")
    void givenCreateMealDto_whenSomeDishesNotFound_thenThrowDishNotFoundException() {
        createMealDto.dishesId().add(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(dishRepository.findById(2L)).thenReturn(Optional.empty());
        when(mealMapper.createMealDtoToMeal(createMealDto, user)).thenReturn(meal);

        DishNotFoundException exception = assertThrows(DishNotFoundException.class, () -> mealServiceImpl.addMeal(createMealDto));
        assertThat(exception.getMessage()).isEqualTo("Данные блюда не найдены: [2]");
        verify(userRepository).findById(1L);
        verify(dishRepository).findById(1L);
        verify(dishRepository).findById(2L);
        verifyNoInteractions(mealRepository);
    }

}
