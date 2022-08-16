package com.mathffreitas.app.appws.service;

import com.mathffreitas.app.appws.dto.AddressDto;

import java.util.List;

public interface AddressService {
    List<AddressDto> getAddresses(String userId);
}
