package com.mathffreitas.app.appws.repository;

import com.mathffreitas.app.appws.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID>{
    UserEntity findUserByEmail(String email);
}
