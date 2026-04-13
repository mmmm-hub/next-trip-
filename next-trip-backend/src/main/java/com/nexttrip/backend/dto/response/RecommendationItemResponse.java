package com.nexttrip.backend.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationItemResponse {

	private String destinationId;
	private Double score;

	@Builder.Default
	private List<String> reasons = new ArrayList<>();
}
