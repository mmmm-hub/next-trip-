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
public class DestinationDetailsResponse {

	private String id;
	private String name;
	private String country;
	private String continent;
	private String description;

	private BigDecimal priceFrom;
	private BigDecimal averageHotelPrice;
	private String currency;

	private String climate;

	private String season;

	@Builder.Default
	private List<String> seasonTags = new ArrayList<>();
	@Builder.Default
	private List<String> ambianceTags = new ArrayList<>();
	@Builder.Default
	private List<String> vibes = new ArrayList<>();
	@Builder.Default
	private List<String> tags = new ArrayList<>();
	@Builder.Default
	private List<String> activities = new ArrayList<>();
	@Builder.Default
	private List<String> images = new ArrayList<>();
	@Builder.Default
	private List<String> imageUrls = new ArrayList<>();

	private Double latitude;
	private Double longitude;

	@Builder.Default
	private List<String> badges = new ArrayList<>();

	private Double popularityScore;

	private Boolean visaRequired;
	private Boolean promoted;

	private Double averageRating;
	private Long reviewCount;
}
