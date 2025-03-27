package com.example.nutriplan.mapper;

import com.example.nutriplan.dto.UserDto;
import com.example.nutriplan.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dailyCalorieIntake" , ignore = true)
    User createUserDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);
}
