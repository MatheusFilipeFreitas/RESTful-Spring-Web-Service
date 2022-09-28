package com.mathffreitas.app.appws.model.response;

import com.mathffreitas.app.appws.dto.UserDto;
import com.mathffreitas.app.appws.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserRest extends RepresentationModel<UserRest> {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<AddressesRest> addresses;

    public UserRest() {
    }

    public UserRest(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AddressesRest> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressesRest> addresses) {
        this.addresses = addresses;
    }

    public static UserRest toRest(UserEntity entity) {
        UserRest dto = new UserRest();
        dto = new ModelMapper().map(entity, UserRest.class);
        //TODO: dto.setAddresses();
        return dto;
    }
}
