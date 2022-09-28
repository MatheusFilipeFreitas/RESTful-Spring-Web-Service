package com.mathffreitas.app.appws.service;

import com.mathffreitas.app.appws.dto.UserDto;
import com.mathffreitas.app.appws.model.response.UserRest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto user);
    UserDto getUser(String email);
    UserDto getUserByUserId(String userId);

    UserDto updateUser(String userId, UserDto user);

    void deleteUser(String userId);

    Page<UserRest> getUsers(Pageable pageable);

    boolean verifyEmailToken(String token);

    boolean requestPasswordReset(String email);

    boolean resetPassword(String token, String password);
}
