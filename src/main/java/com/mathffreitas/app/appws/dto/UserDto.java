package com.mathffreitas.app.appws.dto;

import com.mathffreitas.app.appws.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
public class UserDto implements Serializable{

    @Serial
    private static final long serialVersionUID = 6835192601898364280L;
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
    private List<AddressDto> addresses;
    private Collection<String> roles;


    public UserDto() {

    }

    public UserDto(String firstName, String lastName, String email, String password, String encryptedPassword, String emailVerificationToken, Boolean emailVerificationStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.encryptedPassword = encryptedPassword;
        this.emailVerificationToken = emailVerificationToken;
        this.emailVerificationStatus = emailVerificationStatus;
    }

}

