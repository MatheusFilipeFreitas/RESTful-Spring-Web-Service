package com.mathffreitas.app.appws.controller;

import com.mathffreitas.app.appws.dto.UserDto;
import com.mathffreitas.app.appws.exceptions.UserServiceException;
import com.mathffreitas.app.appws.model.response.ErrorMessages;
import com.mathffreitas.app.appws.model.response.UserRest;
import com.mathffreitas.app.appws.model.request.UserDetailsRequestModel;
import com.mathffreitas.app.appws.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }) // MediaType for XML & JSON response type support
    public UserRest getUserById(@PathVariable String id) {
        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    /*
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public String getUser() {
        return "get user was called";
    }
     */

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnValue = new UserRest();

        if(userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty() || userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping
    public String updateUser() {
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user was called";
    }
}
