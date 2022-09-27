package com.mathffreitas.app.appws.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

    @Autowired
    Utils utils;

    @BeforeEach
    void setUp() throws Exception {

    }

    @Test
    final void testGenerateUserId() {
        String userId = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);

        assertNotNull(userId);
        assertNotNull(userId2);

        assertTrue(userId.length() == 30);
        assertTrue(userId2.length() == 30);

        assertTrue(!userId.equalsIgnoreCase(userId2)); //not equal
    }

    @Test
    final void testHasTokenNotExpired() {
        String token = utils.generateEmailVerificationToken("4yr65hhyid84");
        assertNotNull(token);

        boolean hasTokenExpired = Utils.hasTokenExpired(token);
        assertFalse(hasTokenExpired);
    }

    @Test
    @Disabled //token expire: 08/10/2022
    final void testHasTokenExpired() {
        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXRoZmZyZWl0YXNAaG90bWFpbC5jb20iLCJleHAiOjE2NjUxNTA0NDB9.v8nD41YDkz5LjAysrVFYa98oj2mheI9hb-IqdziSxpa3EKUI_SaEKjrUxGWc-nQtv5-63GBES21CT4zfzld8EA";
        boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);

        assertTrue(hasTokenExpired);
    }
}
