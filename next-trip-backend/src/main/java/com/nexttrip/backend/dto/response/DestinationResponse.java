package com.nexttrip.backend.dto.response;

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
public class DestinationResponse {

	private String id;
	private String name;
	private String country;
	private String continent;

	private BigDecimal priceFrom;
	private String currency;

	private String climate;

	@Builder.Default
	private List<String> images = new ArrayList<>();

	@Builder.Default
	private List<String> badges = new ArrayList<>();

	private Double popularityScore;

	private Boolean visaRequired;
	private Boolean promoted;
}
