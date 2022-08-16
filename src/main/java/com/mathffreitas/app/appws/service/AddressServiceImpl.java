package com.mathffreitas.app.appws.service;

import com.mathffreitas.app.appws.dto.AddressDto;
import com.mathffreitas.app.appws.entity.AddressEntity;
import com.mathffreitas.app.appws.entity.UserEntity;
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
        if(userEntity == null) return returnValue;

        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
        for(AddressEntity addressEntity : addresses) {
            returnValue.add(new ModelMapper().map(addressEntity, AddressDto.class));
        }

        return returnValue;
    }
}
