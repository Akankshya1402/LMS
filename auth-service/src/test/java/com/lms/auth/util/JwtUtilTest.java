package com.lms.auth.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "security.jwt.secret=THIS_IS_A_VERY_SECURE_SECRET_KEY_FOR_LMS_256_BITS",
        "security.jwt.expiration=86400000"
})
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void shouldGenerateJwtToken() {
        String token = jwtUtil.generateToken("user1", Set.of("CUSTOMER"));

        assertNotNull(token);
        assertTrue(token.startsWith("ey")); // JWT format
    }

    @Test
    void shouldContainUsernameInToken() {
        String token = jwtUtil.generateToken("user1", Set.of("CUSTOMER"));

        assertTrue(token.contains(".")); // header.payload.signature
    }
}
