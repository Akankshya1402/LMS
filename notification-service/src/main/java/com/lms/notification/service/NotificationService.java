package com.lms.notification.service;

import com.lms.notification.dto.NotificationEvent;
import com.lms.notification.dto.NotificationResponse;

public interface NotificationService {

    NotificationResponse send(NotificationEvent event);
}

