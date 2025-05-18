package com.auth.model.mapper;


import com.auth.model.dto.request.UserDto;
import com.auth.model.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User userEntity);
    User toEntity(UserDto userDto);

}
