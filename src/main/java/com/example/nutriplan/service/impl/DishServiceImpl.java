package com.example.nutriplan.service.impl;

import com.example.nutriplan.dto.DishDto;
import com.example.nutriplan.dto.DishesDto;
import com.example.nutriplan.entity.Dish;
import com.example.nutriplan.mapper.DishMapper;
import com.example.nutriplan.repository.DishRepository;
import com.example.nutriplan.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;
    private final DishRepository dishRepository;

    @Override
    public DishDto addDish(DishDto dishDto) {
        Dish dish = dishMapper.createDishDtoToDish(dishDto);
        var addDish = dishRepository.save(dish);
        return dishMapper.toDishDto(addDish);
    }

    @Override
    public DishesDto getAllDishes() {
        List<DishDto> dishDtoList = dishRepository.findAll()
                .stream()
                .map(dishMapper::toDishDto)
                .toList();
        return new DishesDto(dishDtoList);
    }

}
