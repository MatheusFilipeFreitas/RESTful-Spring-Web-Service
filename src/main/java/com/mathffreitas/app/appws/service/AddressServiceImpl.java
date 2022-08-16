package com.mathffreitas.app.appws.service;

import com.mathffreitas.app.appws.dto.AddressDto;
import com.mathffreitas.app.appws.entity.AddressEntity;
import com.mathffreitas.app.appws.entity.UserEntity;
import com.mathffreitas.app.appws.exceptions.AddressServiceException;
import com.mathffreitas.app.appws.exceptions.UserServiceException;
import com.mathffreitas.app.appws.model.response.error.ErrorMessages;
import com.mathffreitas.app.appws.repository.AddressRepository;
import com.mathffreitas.app.appws.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;
    @Override
    public List<AddressDto> getAddresses(String userId) {
        List<AddressDto> returnValue = new ArrayList<>();

        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) throw new UserServiceException(ErrorMessages.USER_NO_RECORD_FOUND.getErrorMessage());

        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
        for(AddressEntity addressEntity : addresses) {
            returnValue.add(new ModelMapper().map(addressEntity, AddressDto.class));
        }

        return returnValue;
    }

    @Override
    public AddressDto getAddress(String addressId, String userId) {
        AddressDto returnValue = new AddressDto();

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) throw new UserServiceException(ErrorMessages.USER_NO_RECORD_FOUND.getErrorMessage());
        if(addressEntity == null) throw new AddressServiceException(ErrorMessages.ADDRESS_NO_RECORD_FOUND.getErrorMessage());

        returnValue = new ModelMapper().map(addressEntity, AddressDto.class);

        return returnValue;
    }
}
