package com.example.nutriplan.mapper;

import com.example.nutriplan.dto.DishDto;
import com.example.nutriplan.entity.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DishMapper {

    @Mapping(target = "id", ignore = true)
    Dish createDishDtoToDish(DishDto dto);

    DishDto toDishDto(Dish dish);


}
