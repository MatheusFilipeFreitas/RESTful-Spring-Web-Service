package com.mathffreitas.app.appws.repository;

import com.mathffreitas.app.appws.entity.AddressEntity;
import com.mathffreitas.app.appws.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
}
