package com.travelbooking.travelbooking.mapper;
import com.travelbooking.travelbooking.dto.UserRequestDto;
import com.travelbooking.travelbooking.dto.UserResponseDto;
import com.travelbooking.travelbooking.entity.User;

public class UserMapper {

    public static User toEntity(UserRequestDto userRequestDto) {
       User user = new User();

       user.setUsername(userRequestDto.getUsername());
       user.setEmail(userRequestDto.getEmail());
       user.setPassword(userRequestDto.getPassword());
       return user;
    }

    public static UserResponseDto toDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserId(user.getUserId());
        userResponseDto.setUserName(user.getUsername());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setRole(user.getRole().name());
        userResponseDto.setCreatedDate(user.getCreatedDate());
        return userResponseDto;

    }

}
