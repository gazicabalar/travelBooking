package com.travelbooking.travelbooking.service;

import com.travelbooking.travelbooking.dto.UserRequestDto;
import com.travelbooking.travelbooking.dto.UserResponseDto;
import com.travelbooking.travelbooking.entity.User;
import com.travelbooking.travelbooking.entity.Role;
import com.travelbooking.travelbooking.exception.ResourceNotFoundException;
import com.travelbooking.travelbooking.mapper.UserMapper;
import com.travelbooking.travelbooking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = UserMapper.toEntity(userRequestDto);

        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));


        if (userRequestDto.getRole() == null) {
            user.setRole(Role.CUSTOMER);
        } else {
            user.setRole(userRequestDto.getRole());
        }

        user.setCreatedDate(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found" + userId));
        return UserMapper.toDto(user);
    }


    public UserResponseDto findByEmail(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }
}
