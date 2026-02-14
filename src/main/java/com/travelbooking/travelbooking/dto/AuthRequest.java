package com.travelbooking.travelbooking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    public String username;
    public String password;
    public String email;
}
