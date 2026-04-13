package com.nexttrip.backend.dto.request;

import java.math.BigDecimal;
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
public class UserPreferencesRequest {

	@Builder.Default
	private List<String> preferredClimateTags = new ArrayList<>();
	@Builder.Default
	private List<String> preferredAmbianceTags = new ArrayList<>();
	private BigDecimal budgetMin;
	private BigDecimal budgetMax;
}
