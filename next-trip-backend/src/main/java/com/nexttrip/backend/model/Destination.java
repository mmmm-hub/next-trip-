package com.nexttrip.backend.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "destinations")
public class Destination {

	@Id
	private String id;

	private String name;
	private String country;
	private String continent;
	private String description;

	private BigDecimal priceFrom;
	private BigDecimal averageHotelPrice;
	private String currency;

	private String climate;

	@Builder.Default
	private List<String> seasonTags = new ArrayList<>();
	@Builder.Default
	private List<String> ambianceTags = new ArrayList<>();
	@Builder.Default
	private List<String> activities = new ArrayList<>();
	@Builder.Default
	private List<String> images = new ArrayList<>();

	private Double latitude;
	private Double longitude;

	@Builder.Default
	private List<String> badges = new ArrayList<>();

	private Double popularityScore;

	private Boolean visaRequired;
	private Boolean promoted;
}
