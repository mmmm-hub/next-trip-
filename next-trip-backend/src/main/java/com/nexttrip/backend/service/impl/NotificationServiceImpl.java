package com.nexttrip.backend.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nexttrip.backend.dto.response.NotificationResponse;
import com.nexttrip.backend.exception.ResourceNotFoundException;
import com.nexttrip.backend.model.AppNotification;
import com.nexttrip.backend.repository.AppNotificationRepository;
import com.nexttrip.backend.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final AppNotificationRepository repository;

	@Override
	public List<NotificationResponse> listForUser(String email) {
		return repository.findByUserEmailOrderByCreatedAtDesc(email).stream()
				.map(n -> NotificationResponse.builder()
						.id(n.getId())
						.message(n.getMessage())
						.read(n.isRead())
						.createdAt(n.getCreatedAt())
						.build())
				.toList();
	}

	@Override
	public void markRead(String email, String notificationId) {
		AppNotification n = repository.findById(notificationId)
				.orElseThrow(() -> new ResourceNotFoundException("Notification", notificationId));
		if (!email.equalsIgnoreCase(n.getUserEmail())) {
			throw new ResourceNotFoundException("Notification", notificationId);
		}
		n.setRead(true);
		repository.save(n);
	}

	@Override
	public void notifyUser(String email, String message) {
		AppNotification n = AppNotification.builder()
				.userEmail(email)
				.message(message)
				.read(false)
				.createdAt(Instant.now())
				.build();
		repository.save(n);
	}
}
