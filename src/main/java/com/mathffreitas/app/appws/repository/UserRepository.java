package com.mathffreitas.app.appws.repository;

import com.mathffreitas.app.appws.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    UserEntity findUserByEmail(String email);
    UserEntity findByUserId(String userId);

    UserEntity findUserByEmailVerificationToken(String token);

    @Query(value = "SELECT * FROM Users u WHERE u.EMAIL_VERIFICATION_STATUS = true",
            countQuery = "SELECT COUNT(*) FROM Users u WHERE u.EMAIL_VERIFICATION_STATUS = true",
            nativeQuery = true)
    Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);

    @Query(value = "SELECT * FROM Users u WHERE u.FIRST_NAME = ?1", nativeQuery = true) //"?1" is the argument from the method in order of arguments position
    List<UserEntity> findUserByFirstName(String firstName);

    @Query(value = "SELECT * FROM Users u WHERE u.LAST_NAME = :lastName", nativeQuery = true)
    List<UserEntity> findUserByLastName(@Param("lastName") String lastName);

    @Query(value = "SELECT * FROM Users u WHERE u.FIRST_NAME LIKE %:keyword% OR u.LAST_NAME LIKE %:keyword%", nativeQuery = true)
    List<UserEntity> findUserByKeyword(@Param("keyword") String keyword);

//    begin with = :keyword%
//    end with = %:keyword
//    contains = %:keyword%

    @Query(value = "SELECT u.FIRST_NAME, u.LAST_NAME FROM Users u WHERE u.FIRST_NAME LIKE %:keyword% OR u.LAST_NAME LIKE %:keyword%", nativeQuery = true)
    List<Object[]> findUserFirstNameAndLastNameByKeyword(@Param("keyword") String keyword);

    @Transactional //Service and Controller for Update, Delete
    @Modifying
    @Query(value = "UPDATE Users u SET u.EMAIL_VERIFICATION_STATUS = :emailVerificationStatus WHERE u.USER_ID = :userId", nativeQuery = true)
    void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus, @Param("userId") String userId);

    @Query("SELECT user FROM UserEntity user WHERE user.userId = :userId")
    UserEntity findUserEntityByUserId(@Param("userId") String userId);

    @Query("SELECT user.firstName, user.lastName FROM UserEntity user WHERE user.userId = :userId")
    List<Object[]> getUserEntityFullNameById(@Param("userId") String userId);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.emailVerificationStatus = :emailVerificationStatus WHERE u.userId = :userId")
    void updateUserEntityEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus, @Param("userId") String userId);
}
