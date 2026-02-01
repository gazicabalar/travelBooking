package com.travelbooking.travelbooking.service;

import com.travelbooking.travelbooking.dto.UserRequestDto;
import com.travelbooking.travelbooking.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto getUserById(Long userId);

    List<UserResponseDto> getAllUsers();

}
