package com.example.nutriplan.service;

import com.example.nutriplan.dto.DishDto;
import com.example.nutriplan.dto.DishesDto;
import com.example.nutriplan.entity.Dish;
import com.example.nutriplan.mapper.DishMapper;
import com.example.nutriplan.repository.DishRepository;
import com.example.nutriplan.service.impl.DishServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DishServiceImplTest {

    @Mock
    private DishMapper dishMapper;
    @Mock
    private DishRepository dishRepository;
    @InjectMocks
    private DishServiceImpl dishService;

    private DishDto dishDto;
    private Dish dish;

    @BeforeEach
    void setUp() {
        dishDto = new DishDto(null, "Salad", 150.0, 5.0, 10.0, 15.0);
        dish = new Dish();
        dish.setName("Salad");
        dish.setCaloriesPerServing(150.0);
        dish.setProteins(5.0);
        dish.setFats(10.0);
        dish.setCarbohydrates(15.0);

    }

    @Test
    @DisplayName("Успешное добавление блюда")
    public void givenDishDto_whenAddDish_thenSaveDishAndReturnDishDto() {
        when(dishMapper.createDishDtoToDish(dishDto)).thenReturn(dish);
        when(dishRepository.save(dish)).thenReturn(dish);
        when(dishMapper.toDishDto(dish)).thenReturn(dishDto);

        DishDto savedDish = dishService.addDish(dishDto);

        assertNotNull(savedDish);
        assertEquals(dishDto, savedDish);
        assertEquals("Salad", savedDish.name());
        assertEquals(150.0, savedDish.caloriesPerServing());

        InOrder inOrder = inOrder(dishMapper, dishRepository);

        inOrder.verify(dishMapper, times(1)).createDishDtoToDish(dishDto);
        inOrder.verify(dishRepository, times(1)).save(dish);
        inOrder.verify(dishMapper, times(1)).toDishDto(dish);
    }

    @Test
    @DisplayName("Получение всех блюд - успешный случай")
    void whenGetAllDishes_thenReturnDishesDto() {
        List<Dish> dishes = List.of(dish);

        when(dishRepository.findAll()).thenReturn(dishes);
        when(dishMapper.toDishDto(any(Dish.class))).thenReturn(dishDto);

        DishesDto result = dishService.getAllDishes();

        assertNotNull(result);
        assertEquals(dishes.size(), result.dishes().size());
        verify(dishRepository, times(1)).findAll();
        verify(dishMapper, times(1)).toDishDto(dish);
    }


}



