package com.mathffreitas.app.appws.service;

import com.mathffreitas.app.appws.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto user);
    UserDto getUser(String email);
}
