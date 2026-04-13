package com.nexttrip.backend.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class AppNotification {

	@Id
	private String id;

	@Indexed
	private String userEmail;

	private String message;

	@Builder.Default
	private boolean read = false;

	private Instant createdAt;
}
