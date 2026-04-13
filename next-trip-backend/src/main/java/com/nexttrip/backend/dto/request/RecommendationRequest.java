package com.nexttrip.backend.dto.request;

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
public class RecommendationRequest {

	private Double minBudget;
	private Double maxBudget;
	private String climate;
	private String season;

	@Builder.Default
	private List<String> ambianceTags = new ArrayList<>();

	@Builder.Default
	private Integer limit = 6;
}
