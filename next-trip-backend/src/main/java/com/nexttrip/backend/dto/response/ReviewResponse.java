package com.nexttrip.backend.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

	private String id;
	private String destinationId;
	private String userId;
	private String authorName;

	private Integer rating;
	private String comment;

	private Instant createdAt;
}
