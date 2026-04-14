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

	/** Saison idéale (libellé court, ex. été) */
	private String season;

	@Builder.Default
	private List<String> seasonTags = new ArrayList<>();
	@Builder.Default
	private List<String> ambianceTags = new ArrayList<>();
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

	/** Ambiances / vibes marketing (affichage catalogue) */
	@Builder.Default
	private List<String> vibes = new ArrayList<>();

	/** Tags libres (thèmes, mots-clés) */
	@Builder.Default
	private List<String> tags = new ArrayList<>();

	private Double popularityScore;

	private Boolean visaRequired;
	private Boolean promoted;
}
