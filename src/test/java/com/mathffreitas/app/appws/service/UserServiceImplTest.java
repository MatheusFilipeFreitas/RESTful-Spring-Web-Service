package com.mathffreitas.app.appws.service;

import com.mathffreitas.app.appws.activity.EmailSender;
import com.mathffreitas.app.appws.dto.AddressDto;
import com.mathffreitas.app.appws.dto.UserDto;
import com.mathffreitas.app.appws.entity.AddressEntity;
import com.mathffreitas.app.appws.entity.UserEntity;
import com.mathffreitas.app.appws.exceptions.UserServiceException;
import com.mathffreitas.app.appws.repository.UserRepository;
import com.mathffreitas.app.appws.service.UserServiceImpl;
import com.mathffreitas.app.appws.shared.AmazonSES;
import com.mathffreitas.app.appws.shared.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    Utils utils;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    EmailSender mailSender;
    String userId = "asuhsauhsauh";
    String encryptedPassword = "74asuhsauhasuhas";
    UserEntity userEntity;
    /*
    @Mock
    AmazonSES amazonSES;
    */

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Test");
        userEntity.setUserId(userId);
        userEntity.setEmail("test@test.com");
        userEntity.setEmailVerificationToken("asuhasuhasuash");
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setAddresses(getAddressesEntity());
    }

    @Test
    final void testGetUser() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUser("test@test.com");

        assertNotNull(userDto);
        assertEquals("Test", userDto.getFirstName());
    }

    @Test
    final void testGetUser_UserServiceException() {
        when(userRepository.findUserByEmail( anyString())).thenReturn(null);
        assertThrows(UserServiceException.class, ()-> {
            userService.getUser("test@test.com");
        });
    }

    @Test
    final void testCreateUser_UserServiceException() {
        when(userRepository.findUserByEmail( anyString())).thenReturn(userEntity);

        UserDto userDto = new UserDto();
        userDto.setFirstName("Teste");
        userDto.setLastName("Test");
        userDto.setPassword("123456");
        userDto.setEmail("test@test.com");
        userDto.setAddresses(getAddressesDto());

        assertThrows(UserServiceException.class, ()-> {
            userService.createUser(userDto);
        });
    }

    @Test
    final void testCreateUser() {
        when(userRepository.findUserByEmail( anyString())).thenReturn(null);
        when(utils.generateAddressId( anyInt())).thenReturn("884asuhasuhsa");
        when(utils.generateUserId( anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode( anyString() )).thenReturn(encryptedPassword);
        when(userRepository.save( any(UserEntity.class))).thenReturn(userEntity);
        //Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));

        UserDto userDto = new UserDto();
        userDto.setFirstName("Teste");
        userDto.setLastName("Test");
        userDto.setPassword("123456");
        userDto.setEmail("test@test.com");
        userDto.setAddresses(getAddressesDto());

        UserDto storedUserDetails = userService.createUser(userDto);
        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
        assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
        assertNotNull(storedUserDetails.getUserId());
        verify(utils,times(storedUserDetails.getAddresses().size())).generateAddressId(30);
        verify(bCryptPasswordEncoder,times(1)).encode("123456");
        verify(userRepository,times(1)).save(any(UserEntity.class));
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

    private List<AddressEntity> getAddressesEntity() {
        List<AddressDto> addresses = getAddressesDto();

        Type listType = new TypeToken<List<AddressEntity>>(){}.getType();

        return new ModelMapper().map(addresses, listType);
    }

}
