package com.lms.notification.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void shouldReturn404ForNotificationNotFound() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        var response = handler.handleNotFound(
                new NotificationNotFoundException("Not found"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
