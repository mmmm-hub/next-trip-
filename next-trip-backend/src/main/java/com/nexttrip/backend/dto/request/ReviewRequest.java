package com.nexttrip.backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {

	/**
	 * Optionnel si l'identifiant est déjà présent dans l'URL (POST /api/destinations/{id}/reviews).
	 */
	private String destinationId;

	@NotNull
	@Min(1)
	@Max(5)
	private Integer rating;

	private String comment;
}
