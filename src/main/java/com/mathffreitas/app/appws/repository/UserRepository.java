package com.mathffreitas.app.appws.repository;

import com.mathffreitas.app.appws.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, UUID> {
    UserEntity findUserByEmail(String email);
    UserEntity findByUserId(String userId);

    UserEntity findUserByEmailVerificationToken(String token);
}
