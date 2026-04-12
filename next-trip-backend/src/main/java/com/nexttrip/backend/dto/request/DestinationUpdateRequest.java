package com.nexttrip.backend.dto.request;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinationUpdateRequest {

	private String name;
	private String country;
	private String continent;
	private String description;

	private BigDecimal priceFrom;
	private BigDecimal averageHotelPrice;
	private String currency;

	private String climate;

	private List<String> seasonTags;
	private List<String> ambianceTags;
	private List<String> activities;
	private List<String> images;

	private Double latitude;
	private Double longitude;

	private List<String> badges;

	private Double popularityScore;

	private Boolean visaRequired;
	private Boolean promoted;
}
