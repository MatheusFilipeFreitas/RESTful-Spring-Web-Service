package com.mathffreitas.app.appws.controller;

import com.mathffreitas.app.appws.dto.AddressDto;
import com.mathffreitas.app.appws.dto.UserDto;
import com.mathffreitas.app.appws.model.response.UserRest;
import com.mathffreitas.app.appws.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserServiceImpl userService;

    UserDto userDto;

    final String USER_ID = "ashsauhUAHAUSHUdsah01289u1230";

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userDto = new UserDto();
        userDto.setFirstName("Teste");
        userDto.setLastName("Test");
        userDto.setPassword("123456");
        userDto.setEmail("test@test.com");
        userDto.setEmailVerificationStatus(Boolean.FALSE);
        userDto.setEmailVerificationToken(null);
        userDto.setUserId(USER_ID);
        userDto.setAddresses(getAddressesDto());
        userDto.setEncryptedPassword("sauhusahsauhsau");
    }

    @Test
    final void testGetUser() {
        when(userService.getUserByUserId(anyString())).thenReturn(userDto);

        UserRest userRest = userController.getUserById(USER_ID);

        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getUserId());
        assertEquals(userDto.getFirstName(), userRest.getFirstName());
        assertEquals(userDto.getLastName(), userRest.getLastName());
        assertTrue(userDto.getAddresses().size() == userRest.getAddresses().size());
    }

    private List<AddressDto> getAddressesDto() {
        AddressDto shippingAddressDto = new AddressDto();
        shippingAddressDto.setCity("Sao paulo");
        shippingAddressDto.setCountry("Brasil");
        shippingAddressDto.setPostalCode("ABC123");
        shippingAddressDto.setStreetName("123 Street name");
        shippingAddressDto.setType("shipping");

        AddressDto billingAddressDto = new AddressDto();
        billingAddressDto.setCity("Sao paulo");
        billingAddressDto.setCountry("Brasil");
        billingAddressDto.setPostalCode("ABC123");
        billingAddressDto.setStreetName("123 Street name");
        billingAddressDto.setType("shipping");

        List<AddressDto> addresses = new ArrayList<>();
        addresses.add(billingAddressDto);
        addresses.add(shippingAddressDto);

        return addresses;
    }
}
