package com.teclead.ventures.javatask.mapper;

import com.teclead.ventures.javatask.dto.RequestUserDto;
import com.teclead.ventures.javatask.dto.ResponseUserDto;
import com.teclead.ventures.javatask.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    ResponseUserDto map(User user);

    User map(RequestUserDto userDto);
}
