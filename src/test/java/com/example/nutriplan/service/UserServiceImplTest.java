package com.example.nutriplan.service;

import com.example.nutriplan.dto.*;
import com.example.nutriplan.entity.Meal;
import com.example.nutriplan.entity.User;
import com.example.nutriplan.exeption.UserNotFoundException;
import com.example.nutriplan.mapper.MealMapper;
import com.example.nutriplan.mapper.UserMapper;
import com.example.nutriplan.repository.MealRepository;
import com.example.nutriplan.repository.UserRepository;
import com.example.nutriplan.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.nutriplan.enums.Goal.WEIGHT_LOSS;
import static com.example.nutriplan.enums.MealType.BREAKFAST;
import static com.example.nutriplan.enums.MealType.LUNCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private MealMapper mealMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;
    private UserDto userDto;
    private Meal meal1;
    private Meal meal2;
    private MealDto mealDto1;
    private MealDto mealDto2;
    private TimeDto timeDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "John", "asdad@gmail.com", 25, 70.0, 170.0, WEIGHT_LOSS, 1500.0);
        user = new User();
        user.setId(1L);
        user.setUsername("John");
        user.setWeight(70.0);
        user.setHeight(170.0);
        user.setAge(25);
        user.setGoal(WEIGHT_LOSS);
        user.setDailyCalorieIntake(1500.0);

        meal1 = new Meal();
        meal1.setId(1L);
        meal1.setCreateMealDate(LocalDate.of(2023, 10, 1).atStartOfDay());

        meal2 = new Meal();
        meal2.setId(2L);
        meal2.setCreateMealDate(LocalDate.of(2023, 10, 2).atStartOfDay());

        mealDto1 = new MealDto(
                1L,
                500.0,
                BREAKFAST,
                LocalDate.of(2023, 10, 1).atStartOfDay(),
                null // dishes
        );

        mealDto2 = new MealDto(
                2L,
                700.0,
                LUNCH,
                LocalDate.of(2023, 10, 2).atStartOfDay(),
                null // dishes
        );

        timeDto = new TimeDto(
                LocalDate.of(2023, 10, 1),
                1L
        );

    }

    @Test
    @DisplayName("Успешное добавление нового пользователя")
    public void givenUserDto_whenCreateUser_thenSaveUserAndReturnUserDto() {
        when(userMapper.createUserDtoToUser(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserDto(user)).thenReturn(userDto);

        UserDto result = userServiceImpl.createUser(userDto);

        assertNotNull(result);
        assertEquals(userDto, result);

        InOrder inOrder = inOrder(userMapper, userRepository, userMapper);

        inOrder.verify(userMapper, times(1)).createUserDtoToUser(userDto);
        inOrder.verify(userRepository, times(1)).save(user);
        inOrder.verify(userMapper, times(1)).userToUserDto(user);

    }


    @Test
    @DisplayName("Корректное вычисление BMR на основе данных пользователя")
    void givenUserData_whenCalculateBMR_thenReturnCorrectBMR() {

        when(userMapper.createUserDtoToUser(userDto)).thenReturn(user);

        User createdUser = userMapper.createUserDtoToUser(userDto);
        double calculatedBMR = userServiceImpl.calculateBMR(
                createdUser.getWeight(),
                createdUser.getHeight(),
                createdUser.getAge(),
                createdUser.getGoal()
        );

        assertThat(calculatedBMR).isEqualTo(2030.1875);

    }

    @Test
    @DisplayName("Должно выбрасывать исключение, если пользователь не найден при получении истории приемов пищи")
    void givenUserId_whenUserNotFound_thenThrowUserNotFoundException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getHistoryToDays(1L));

        verify(userRepository, times(1)).existsById(1L);
        verifyNoInteractions(mealRepository, mealMapper);

    }

    @Test
    @DisplayName("Должно корректно возвращать историю приемов пищи, сгруппированную по дням")
    void givenUserId_whenUserExists_thenReturnMealsHistoryGroupedByDate() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(mealRepository.findMealsByUserId(1L)).thenReturn(List.of(meal1, meal2));
        when(mealMapper.mealToMealDto(meal1)).thenReturn(mealDto1);
        when(mealMapper.mealToMealDto(meal2)).thenReturn(mealDto2);

        MealsDto result = userServiceImpl.getHistoryToDays(1L);

        Map<LocalDate, List<MealDto>> expectedMap = Map.of(
                LocalDate.of(2023, 10, 1), List.of(mealDto1),
                LocalDate.of(2023, 10, 2), List.of(mealDto2)
        );
        assertThat(result.localDateTimeListMap()).isEqualTo(expectedMap);
        verify(userRepository).existsById(1L);
        verify(mealRepository).findMealsByUserId(1L);
        verify(mealMapper).mealToMealDto(meal1);
        verify(mealMapper).mealToMealDto(meal2);
    }

    @Test
    @DisplayName("Должно вернуть пустую карту, если у пользователя нет приемов пищи")
    void givenUserId_whenNoMealsFound_thenReturnEmptyMap() {

        when(userRepository.existsById(1L)).thenReturn(true);
        when(mealRepository.findMealsByUserId(1L)).thenReturn(List.of());

        MealsDto result = userServiceImpl.getHistoryToDays(1L);

        assertThat(result.localDateTimeListMap()).isEmpty();

        verify(userRepository).existsById(1L);
        verify(mealRepository).findMealsByUserId(1L);
        verifyNoInteractions(mealMapper);
    }

    @Test
    @DisplayName("Должно вернуть true, если количество потребленных калорий в пределах дневного лимита")
    void givenTimeDto_whenCaloriesWithinLimit_thenReturnTrue() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealRepository.calculateTotalCaloriesForDay(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(1300.0);

        Boolean result = userServiceImpl.calculateCaloriesPerDay(timeDto);

        assertTrue(result);
        verify(userRepository).findById(1L);
        verify(mealRepository).calculateTotalCaloriesForDay(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Должно вернуть false, если количество потребленных калорий превышает дневной лимит")
    void givenTimeDto_whenCaloriesExceedLimit_thenReturnFalse() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealRepository.calculateTotalCaloriesForDay(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(2500.0);

        Boolean result = userServiceImpl.calculateCaloriesPerDay(timeDto);

        assertFalse(result);
        verify(userRepository).findById(1L);
        verify(mealRepository).calculateTotalCaloriesForDay(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Должно выбрасывать исключение, если пользователь не найден при расчете калорий за день")
    void givenTimeDto_whenUserNotFound_thenThrowUserNotFoundException() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.calculateCaloriesPerDay(timeDto));

        verify(userRepository).findById(1L);
        verifyNoInteractions(mealRepository);
    }

    @Test
    @DisplayName("Должно возвращать список приемов пищи и суммарные калории, если пользователь существует и у него есть приемы пищи")
    void givenUserExistsAndHasMeals_whenGetDailyCaloriesAndMeals_thenReturnMealsAndTotalCalories() {

        when(userRepository.existsById(1L)).thenReturn(true);
        when(mealRepository.findMealsForUserOnDate(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(meal1, meal2));
        when(mealMapper.mealToMealDto(meal1)).thenReturn(mealDto1);
        when(mealMapper.mealToMealDto(meal2)).thenReturn(mealDto2);

        MealsToTimeDto expectedDto = new MealsToTimeDto(
                List.of(mealDto1, mealDto2),
                1200.0
        );
        when(mealMapper.mapMealsToMealsToTimeDto(anyList(), anyDouble()))
                .thenReturn(expectedDto);

        MealsToTimeDto result = userServiceImpl.getDailyCaloriesAndMeals(timeDto);

        assertThat(result.mealDtoList()).isEqualTo(expectedDto.mealDtoList());
        assertThat(result.calories()).isEqualTo(expectedDto.calories());

        verify(userRepository).existsById(1L);
        verify(mealRepository).findMealsForUserOnDate(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(mealMapper).mealToMealDto(meal1);
        verify(mealMapper).mealToMealDto(meal2);
    }

    @Test
    @DisplayName("Должно возвращать пустой список приемов пищи и 0 калорий, если пользователь существует, но приемов пищи не найдено")
    void givenUserExistsButNoMealsFound_whenGetDailyCaloriesAndMeals_thenReturnEmptyMealsAndZeroCalories() {

        when(userRepository.existsById(1L)).thenReturn(true);
        when(mealRepository.findMealsForUserOnDate(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        MealsToTimeDto expectedDto = new MealsToTimeDto(
                List.of(),
                0.0
        );

        when(mealMapper.mapMealsToMealsToTimeDto(anyList(), anyDouble()))
                .thenReturn(expectedDto);

        MealsToTimeDto result = userServiceImpl.getDailyCaloriesAndMeals(timeDto);

        assertThat(result.mealDtoList()).isEmpty();
        assertThat(result.calories()).isEqualTo(0.0);

        verify(userRepository).existsById(1L);
        verify(mealRepository).findMealsForUserOnDate(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(mealMapper).mapMealsToMealsToTimeDto(anyList(), anyDouble());
    }

    @Test
    @DisplayName("Должно выбрасывать исключение, если указанный пользователь отсутствует")
    void givenNonExistentUser_whenGetDailyCaloriesAndMeals_thenThrowUserNotFoundException() {

        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getDailyCaloriesAndMeals(timeDto));

        verify(userRepository).existsById(1L);
        verifyNoInteractions(mealRepository, mealMapper);
    }
}


