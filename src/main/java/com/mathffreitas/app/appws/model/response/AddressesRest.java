package com.mathffreitas.app.appws.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressesRest extends RepresentationModel<AddressesRest> {
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;

}
