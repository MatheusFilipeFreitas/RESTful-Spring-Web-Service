package com.mathffreitas.app.appws.model.response;

import com.mathffreitas.app.appws.dto.UserDto;
import com.mathffreitas.app.appws.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRest extends RepresentationModel<UserRest> {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<AddressesRest> addresses;


    public UserRest(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public static UserRest toRest(UserEntity entity) {
        UserRest dto = new UserRest();
        dto = new ModelMapper().map(entity, UserRest.class);
        //TODO: dto.setAddresses();
        return dto;
    }
}
