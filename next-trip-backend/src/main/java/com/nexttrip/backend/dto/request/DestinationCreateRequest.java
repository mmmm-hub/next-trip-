package com.nexttrip.backend.dto.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinationCreateRequest {

	@NotBlank
	private String name;
	@NotBlank
	private String country;
	@NotBlank
	private String continent;

	private String description;

	@NotNull
	private BigDecimal priceFrom;
	private BigDecimal averageHotelPrice;
	@NotBlank
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
}
