package com.nexttrip.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexttrip.backend.dto.response.NotificationResponse;
import com.nexttrip.backend.security.SecurityUtils;
import com.nexttrip.backend.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping("/me")
	public ResponseEntity<List<NotificationResponse>> mine() {
		String email = SecurityUtils.currentUserEmail();
		if (email == null) {
			return ResponseEntity.status(401).build();
		}
		return ResponseEntity.ok(notificationService.listForUser(email));
	}

	@PatchMapping("/{id}/read")
	public ResponseEntity<Void> markRead(@PathVariable String id) {
		String email = SecurityUtils.currentUserEmail();
		if (email == null) {
			return ResponseEntity.status(401).build();
		}
		notificationService.markRead(email, id);
		return ResponseEntity.noContent().build();
	}
}
