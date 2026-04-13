package com.nexttrip.backend.service;

import java.util.List;

import com.nexttrip.backend.dto.response.NotificationResponse;

public interface NotificationService {

	List<NotificationResponse> listForUser(String email);

	void markRead(String email, String notificationId);

	void notifyUser(String email, String message);
}
