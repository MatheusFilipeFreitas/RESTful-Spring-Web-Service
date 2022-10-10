package com.mathffreitas.app.appws.service;

import com.mathffreitas.app.appws.activity.EmailSender;
import com.mathffreitas.app.appws.dto.AddressDto;
import com.mathffreitas.app.appws.dto.UserDto;
import com.mathffreitas.app.appws.entity.PasswordResetTokenEntity;
import com.mathffreitas.app.appws.entity.RoleEntity;
import com.mathffreitas.app.appws.entity.UserEntity;
import com.mathffreitas.app.appws.exceptions.UserServiceException;
import com.mathffreitas.app.appws.model.response.UserRest;
import com.mathffreitas.app.appws.model.response.error.ErrorMessages;
import com.mathffreitas.app.appws.repository.PasswordResetTokenRepository;
import com.mathffreitas.app.appws.repository.RoleRepository;
import com.mathffreitas.app.appws.repository.UserRepository;
import com.mathffreitas.app.appws.security.UserPrincipal;
import com.mathffreitas.app.appws.shared.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

//TODO: Block adm
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    AddressService addressService;

    @Autowired
    Utils utils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EmailSender mailSender;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public UserDto createUser(UserDto user) {
        ModelMapper modelMapper = new ModelMapper();

        if(userRepository.findUserByEmail(user.getEmail()) != null) throw new UserServiceException(ErrorMessages.USER_RECORD_ALREADY_EXISTS.getErrorMessage());

        for(int i = 0; i < user.getAddresses().size(); i++) {
            AddressDto address = user.getAddresses().get(i);
            address.setUserDetails(user);
            address.setAddressId(utils.generateAddressId(30));
            user.getAddresses().set(i, address);
        }

        //BeanUtils.copyProperties(user, userEntity);
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);


        //generate extra info to userEntity
        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
        userEntity.setEmailVerificationStatus(false);


        //set roles
        Collection<RoleEntity> roleEntities = new HashSet<>();
        for(String role: user.getRoles()){
            RoleEntity roleEntity = roleRepository.findByName(role);
            if(roleEntity != null) {
                roleEntities.add(roleEntity);
            }
        }

        userEntity.setRoles(roleEntities);

        UserEntity storedUserDetail = userRepository.save(userEntity);

        //BeanUtils.copyProperties(storedUserDetail, returnValue);
        UserDto returnValue = modelMapper.map(storedUserDetail, UserDto.class);

        // Send an email message to verify their email address
        String link = "http://localhost:8080/app-ws/users/email-verification?token=" + userEntity.getEmailVerificationToken();

        try {
            mailSender.sendVerification(userEntity.getEmail(), mailSender.buildEmailVerificationToken(userEntity.getFirstName(), userEntity.getLastName(), link));
        }catch (Exception e){
            e.printStackTrace();
        }

        //new AmazonSES().verifyEmail(returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        ModelMapper modelMapper = new ModelMapper();

        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) throw new UserServiceException(ErrorMessages.USER_NO_RECORD_FOUND.getErrorMessage());

        returnValue = modelMapper.map(userEntity, UserDto.class);

        return returnValue;
    }


    @Override
    public UserDto getUser(String email) {
        UserDto returnValue = new UserDto();

        UserEntity userEntity = userRepository.findUserByEmail(email);
        if(userEntity  == null) throw new UserServiceException(ErrorMessages.USER_NO_RECORD_FOUND.getErrorMessage());

        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto user) {
        UserDto returnValue = new UserDto();
        ModelMapper modelMapper = new ModelMapper();

        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) throw new UserServiceException(ErrorMessages.USER_NO_RECORD_FOUND.getErrorMessage());

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());

        UserEntity updatedUserDetail = userRepository.save(userEntity);

        returnValue = modelMapper.map(updatedUserDetail, UserDto.class);

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) throw new UserServiceException(ErrorMessages.USER_NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(userEntity);
    }

    @Override
    public Page<UserRest> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserRest::toRest);
    }

    @Override
    public boolean verifyEmailToken(String token) {
        boolean returnValue = false;

        // Find user by token
        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if(userEntity == null) throw new UserServiceException(ErrorMessages.AUTHENTICATION_ERROR.getErrorMessage());

        boolean hasTokenExpired = Utils.hasTokenExpired(token);
        if(!hasTokenExpired) {
        userEntity.setEmailVerificationToken(null);
        userEntity.setEmailVerificationStatus(Boolean.TRUE);
        userRepository.save(userEntity);
        returnValue = true;

        }
        return returnValue;
    }

    @Override
    public boolean requestPasswordReset(String email) {
        boolean returnValue = false;
        UserEntity userEntity = userRepository.findUserByEmail(email);

        if(userEntity == null) throw new UserServiceException(ErrorMessages.USER_NO_RECORD_FOUND.getErrorMessage());

        String token = new Utils().generatePasswordResetToken(userEntity.getUserId());

        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(userEntity);
        passwordResetTokenRepository.save(passwordResetTokenEntity);

        /*
            returnValue = new AmazonSES().sendPasswordResetRequest(
                userEntity.getFirstName(),
                userEntity.getEmail(),
                token);
         */

        String link = "http://localhost:8080/app-ws/users/password-reset-request?token=" + token;
        try {
            returnValue = mailSender.sendPasswordReset(userEntity.getEmail(), mailSender.buildPasswordResetTokenEmail(userEntity.getFirstName(), userEntity.getLastName(), link));
        }catch (Exception e){
            throw new RuntimeException("The email with the password was not sent");
        }

        return returnValue;
    }

    @Override
    public boolean resetPassword(String token, String password) {
        boolean returnValue = false;

        if(Utils.hasTokenExpired(token)) throw new UserServiceException(ErrorMessages.USER_PASSWORD_TOKEN_EXPIRED.getErrorMessage());

        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

        if(passwordResetTokenEntity == null) throw new UserServiceException(ErrorMessages.USER_PASSWORD_TOKEN_NOT_FOUND.getErrorMessage());

        // Prepare new password
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        // Update User password in database
        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setEncryptedPassword(encodedPassword);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        // Verify if password was saved successfully
        if(savedUserEntity != null && savedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)){
            returnValue = true;
        }

        // Remove Password Reset token from database
        passwordResetTokenRepository.delete(passwordResetTokenEntity);

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserByEmail(email);

        if(userEntity == null) throw new UserServiceException(ErrorMessages.USER_NO_RECORD_FOUND.getErrorMessage());

        return new UserPrincipal(userEntity);
//        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), userEntity.getEmailVerificationStatus(), true, true, true, new ArrayList<>());

        //return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

}
