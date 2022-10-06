package com.mathffreitas.app.appws.controller;

import com.mathffreitas.app.appws.dto.AddressDto;
import com.mathffreitas.app.appws.dto.UserDto;
import com.mathffreitas.app.appws.exceptions.UserServiceException;
import com.mathffreitas.app.appws.model.request.PasswordResetModel;
import com.mathffreitas.app.appws.model.request.PasswordResetRequestModel;
import com.mathffreitas.app.appws.model.response.*;
import com.mathffreitas.app.appws.model.request.UserDetailsRequestModel;
import com.mathffreitas.app.appws.model.response.error.ErrorMessages;
import com.mathffreitas.app.appws.model.response.operation.OperationStatusModel;
import com.mathffreitas.app.appws.model.response.operation.RequestOperationName;
import com.mathffreitas.app.appws.model.response.operation.RequestOperationStatus;
import com.mathffreitas.app.appws.service.AddressService;
import com.mathffreitas.app.appws.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
//@CrossOrigin(origins = "*") //(origins = "http://localhost:4042") -> specific origin; (origins = {"http://localhost:8888","http://localhost:4042"}) -> multiple origins
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @ApiOperation(value = "Create a new User")
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnValue = new UserRest();

        if(userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty() || userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        //UserDto userDto = new UserDto();
        //BeanUtils.copyProperties(userDetails, userDto);
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @ApiOperation(value = "Get a page of Users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") //in value, its getting the userController.authorizationHeader.description of application.properties
    })
    @GetMapping
    public ResponseEntity<Page<UserRest>> getUsers(@PageableDefault(page = 0, size = 10) Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers(pageable));
    }

    @ApiOperation(value = "Get an Users by public userID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") //in value, its getting the userController.authorizationHeader.description of application.properties
    })
    @GetMapping(path = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }) // MediaType for XML & JSON response type support
    public EntityModel<UserRest> getUserById(@PathVariable String userId) {
        UserRest returnValue = new UserRest();
        ModelMapper modelMapper = new ModelMapper();

        UserDto userDto = userService.getUserByUserId(userId);

        returnValue = modelMapper.map(userDto, UserRest.class);

        for(AddressesRest addressRest : returnValue.getAddresses()) {
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(addressRest.getAddressId(), userId))
                    .withSelfRel();

            addressRest.add(selfLink);
        }

        Link userAddressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId))
                //.slash(userId)
                //.slash("addresses")
                .withRel("addresses");

        Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(userId)
                .withSelfRel();

        return EntityModel.of(returnValue, Arrays.asList(selfLink, userAddressesLink));
    }

    @ApiOperation(value = "Get a List of Addresses from a specific User by public userID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") //in value, its getting the userController.authorizationHeader.description of application.properties
    })
    @GetMapping(path = "/{userId}/addresses", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }) // MediaType for XML & JSON response type support
    public CollectionModel<AddressesRest> getUserAddresses(@PathVariable String userId) {
        List<AddressesRest> returnValue = new ArrayList<>();

        List<AddressDto> addressDto = addressService.getAddresses(userId);

        if(addressDto != null && !addressDto.isEmpty()) {
            Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
            returnValue = new ModelMapper().map(addressDto, listType);

            for(AddressesRest addressRest : returnValue) {

                // http://localhost:8080/users/<userId>/addresses/<addressId>
                Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(addressRest.getAddressId(), userId))
                        //.slash(userId)
                        //.slash("addresses")
                        //.slash(addressId)
                        .withSelfRel();

                addressRest.add(selfLink);
            }
        }

        // http://localhost:8080/users/<userId>
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(userId)
                .withRel("user");

        // http://localhost:8080/users/<userId>/addresses
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId))
                //.slash(userId)
                //.slash("addresses")
                //.slash(addressId)
                .withSelfRel();

        return CollectionModel.of(returnValue, userLink, selfLink);
    }

    @ApiOperation(value = "Get an specific Address by public addressID from a specific User by public userID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") //in value, its getting the userController.authorizationHeader.description of application.properties
    })
    @GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public EntityModel<AddressesRest> getUserAddress(@PathVariable String addressId, @PathVariable String userId) {
        AddressDto addressDto = addressService.getAddress(addressId, userId);

        ModelMapper modelMapper = new ModelMapper();
        AddressesRest returnValue = modelMapper.map(addressDto, AddressesRest.class);

        // http://localhost:8080/users/<userId>
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(userId)
                .withRel("user");

        // http://localhost:8080/users/<userId>/addresses
        Link userAddressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId))
                //.slash(userId)
                //.slash("addresses")
                .withRel("addresses");

        // http://localhost:8080/users/<userId>/addresses/<addressId>
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(addressId, userId))
                //.slash(userId)
                //.slash("addresses")
                //.slash(addressId)
                .withSelfRel();

        //returnValue.add(userLink);
        //returnValue.add(userAddressesLink);
        //returnValue.add(selfLink);

        return EntityModel.of(returnValue, Arrays.asList(userLink, userAddressesLink, selfLink));
    }

    // http://localhost:8080/app-ws/users/email-verification?token=<token>

    //Specific method
    //@CrossOrigin(origins = "*") (origins = "http://localhost:4042") -> specific origin; (origins = {"http://localhost:8888","http://localhost:4042"}) -> multiple origins
    @GetMapping(path = "/email-verification", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

        boolean isVerified = userService.verifyEmailToken(token);

        if(isVerified) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }else{
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;
    }

    @ApiOperation(value = "Update an User by public userID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") //in value, its getting the userController.authorizationHeader.description of application.properties
    })
    @PatchMapping(path = "/{userId}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public UserRest updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        //if(userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updateUser = userService.updateUser(userId, userDto);
        returnValue = new ModelMapper().map(updateUser, UserRest.class);

        System.out.println(returnValue.getAddresses().size());

        return returnValue;
    }

    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Delete an User by public userID")
    @DeleteMapping(path = "/{userId}")
    public OperationStatusModel deleteUser(@PathVariable String userId) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(userId);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    // http://localhost:8080/app-ws/users/password-reset-request
    @PostMapping(path = "/password-reset-request", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {
        OperationStatusModel returnValue = new OperationStatusModel();

        boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());

        returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        if(operationResult){
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }

    @PostMapping(path = "/password-reset", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
        OperationStatusModel returnValue = new OperationStatusModel();

        boolean operationResult = userService.resetPassword(passwordResetModel.getToken(), passwordResetModel.getPassword());

        returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        if(operationResult){
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }
}
