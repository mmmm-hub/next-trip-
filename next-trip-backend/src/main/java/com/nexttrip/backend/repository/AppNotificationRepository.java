package com.nexttrip.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nexttrip.backend.model.AppNotification;

public interface AppNotificationRepository extends MongoRepository<AppNotification, String> {

	List<AppNotification> findByUserEmailOrderByCreatedAtDesc(String userEmail);
}
