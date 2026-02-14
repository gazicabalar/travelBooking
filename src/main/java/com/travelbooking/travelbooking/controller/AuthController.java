package com.travelbooking.travelbooking.controller;

import com.travelbooking.travelbooking.dto.AuthRequest;
import com.travelbooking.travelbooking.dto.AuthResponse;
import com.travelbooking.travelbooking.dto.UserRequestDto;
import com.travelbooking.travelbooking.dto.UserResponseDto;
import com.travelbooking.travelbooking.entity.Role;
import com.travelbooking.travelbooking.exception.BusinessException;
import com.travelbooking.travelbooking.repository.UserRepository;
import com.travelbooking.travelbooking.security.JwtService;
import com.travelbooking.travelbooking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid AuthRequest authRequest) {

        if (userRepository.existsByUsername(authRequest.getUsername())) {
            throw new BusinessException("Username already exists");
        }
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            throw new BusinessException("Email already exists");
        }


        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername(authRequest.getUsername());
        userRequestDto.setEmail(authRequest.getEmail());
        userRequestDto.setPassword(authRequest.getPassword());
        userRequestDto.setRole(Role.CUSTOMER);

        UserResponseDto userResponseDto = userService.createUser(userRequestDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@RequestBody @Valid UserRequestDto userRequestDto) {

        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new BusinessException("Username already exists");
        }
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        userRequestDto.setRole(Role.ADMIN);

        UserResponseDto userResponseDto = userService.createUser(userRequestDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );


        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails.getUsername());


        AuthResponse response = new AuthResponse();
        response.setToken(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
