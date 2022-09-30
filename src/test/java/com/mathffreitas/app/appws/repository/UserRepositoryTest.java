package com.mathffreitas.app.appws.repository;

import com.mathffreitas.app.appws.entity.AddressEntity;
import com.mathffreitas.app.appws.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    static boolean recordsCreated = false;

    @BeforeEach
    void setUp() throws Exception {
        if(!recordsCreated) createRecords();
    }

    @Test
    final void testGetVerifiedUsers() {
        Pageable pageableRequest = PageRequest.of(0, 1);
        Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
        assertNotNull(pages);
        List<UserEntity> userEntities = pages.getContent();
        assertNotNull(userEntities);

        System.out.println(userEntities.size());

        assertTrue(userEntities.size() == 1);
    }

    @Test
    final void testFindUserByFirstName() {
        String firstName = "Matheus";
        List<UserEntity> users = userRepository.findUserByFirstName(firstName);

        assertNotNull(users);
        assertTrue(users.size() == 1);

        UserEntity user = users.get(0);
        assertTrue(user.getFirstName().equals(firstName));
    }

    @Test
    final void testFindUserByLastName() {
        String lastName = "Freitas";
        List<UserEntity> users = userRepository.findUserByLastName(lastName);

        assertNotNull(users);
        assertTrue(users.size() == 1);

        UserEntity user = users.get(0);
        assertTrue(user.getLastName().equals(lastName));
    }

    @Test
    final void testFindUserByKeyword() {
        String keyword = "Fre";
        List<UserEntity> users = userRepository.findUserByKeyword(keyword);

        assertNotNull(users);
        assertTrue(users.size() == 1);

        UserEntity user = users.get(0);
        assertTrue(user.getLastName().contains(keyword) || user.getFirstName().contains(keyword));
    }

    @Test
    final void testFindUserFirstNameAndLastNameByKeyword() {
        String keyword = "Fre";
        List<Object[]> users = userRepository.findUserFirstNameAndLastNameByKeyword(keyword);

        assertNotNull(users);
        assertTrue(users.size() == 1);

        Object[] user = users.get(0);
        String userFistName = String.valueOf(user[0]);
        String userLastName = String.valueOf(user[1]);

        assertNotNull(userFistName);
        assertNotNull(userLastName);

        System.out.println("Fist Name: " + userFistName);
        System.out.println("Last Name: " + userLastName);
    }

    @Test
    final void updateUserEmailVerificationStatus(){
        boolean newEmailVerificationStatus = true;
        userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, "1a2b3c");

        UserEntity storedUserDetails = userRepository.findByUserId("1a2b3c");

        boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();

        assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
    }

    @Test
    final void testFindUserEntityByUserId(){
        String userId = "1a2b3c";
        UserEntity userEntity = userRepository.findUserEntityByUserId(userId);

        assertNotNull(userEntity);
        assertTrue(userEntity.getUserId().equals(userId));
    }

    @Test
    final void testGetUserEntityFullNameById(){
        String userId = "1a2b3c";
        List<Object[]> records = userRepository.getUserEntityFullNameById(userId);

        assertNotNull(records);
        assertTrue(records.size() == 1);

        Object[] userDetails = records.get(0);

        String firstName = String.valueOf(userDetails[0]);
        String lastName = String.valueOf(userDetails[1]);

        assertNotNull(firstName);
        assertNotNull(lastName);

        System.out.println("Fist Name: " + firstName);
        System.out.println("Last Name: " + lastName);
    }

    @Test
    final void updateUserEntityEmailVerificationStatus(){
        boolean newEmailVerificationStatus = true;
        userRepository.updateUserEntityEmailVerificationStatus(newEmailVerificationStatus, "1a2b3c");

        UserEntity storedUserDetails = userRepository.findByUserId("1a2b3c");

        boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();

        assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
    }

    private void createRecords(){
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Matheus");
        userEntity.setLastName("Freitas");
        userEntity.setUserId("1a2b3c");
        userEntity.setEncryptedPassword("xxxx");
        userEntity.setEmail("matheus@freitas.com");
        userEntity.setEmailVerificationStatus(true);

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setType("shipping");
        addressEntity.setAddressId("asuh5uys");
        addressEntity.setCity("Vancouver");
        addressEntity.setCountry("Canada");
        addressEntity.setPostalCode("ABCCBA");
        addressEntity.setStreetName("123 Street Address");

        List<AddressEntity> addresses = new ArrayList<>();
        addresses.add(addressEntity);
        userEntity.setAddresses(addresses);

        userRepository.save(userEntity);

        recordsCreated = true;
    }

}
