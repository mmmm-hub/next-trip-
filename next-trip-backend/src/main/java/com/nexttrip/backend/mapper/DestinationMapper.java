package com.nexttrip.backend.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.nexttrip.backend.dto.request.DestinationCreateRequest;
import com.nexttrip.backend.dto.request.DestinationUpdateRequest;
import com.nexttrip.backend.dto.response.DestinationDetailsResponse;
import com.nexttrip.backend.dto.response.DestinationResponse;
import com.nexttrip.backend.model.Destination;

@Component
public class DestinationMapper {

	public DestinationResponse toResponse(Destination destination) {
		List<String> imgs = mergedImages(destination);
		String primaryImage = imgs.isEmpty() ? null : imgs.get(0);
		List<String> vibeList = resolveVibes(destination);
		return DestinationResponse.builder()
				.id(destination.getId())
				.name(destination.getName())
				.country(destination.getCountry())
				.continent(destination.getContinent())
				.description(destination.getDescription())
				.priceFrom(destination.getPriceFrom())
				.currency(destination.getCurrency())
				.climate(destination.getClimate())
				.season(destination.getSeason())
				.seasonTags(listOrEmpty(destination.getSeasonTags()))
				.vibes(vibeList)
				.tags(listOrEmpty(destination.getTags()))
				.activities(listOrEmpty(destination.getActivities()))
				.images(imgs)
				.imageUrls(new ArrayList<>(imgs))
				.image(primaryImage)
				.badges(listOrEmpty(destination.getBadges()))
				.popularityScore(destination.getPopularityScore())
				.visaRequired(destination.getVisaRequired())
				.promoted(destination.getPromoted())
				.latitude(destination.getLatitude())
				.longitude(destination.getLongitude())
				.build();
	}

	private static List<String> resolveVibes(Destination destination) {
		List<String> v = listOrEmpty(destination.getVibes());
		if (!v.isEmpty()) {
			return v;
		}
		return listOrEmpty(destination.getAmbianceTags());
	}

	/**
	 * Cartographie complète d'une destination (sans agrégats avis — renseignés côté service si besoin).
	 */
	public DestinationDetailsResponse toDetailsResponse(Destination destination) {
		List<String> imgs = mergedImages(destination);
		return DestinationDetailsResponse.builder()
				.id(destination.getId())
				.name(destination.getName())
				.country(destination.getCountry())
				.continent(destination.getContinent())
				.description(destination.getDescription())
				.priceFrom(destination.getPriceFrom())
				.averageHotelPrice(destination.getAverageHotelPrice())
				.currency(destination.getCurrency())
				.climate(destination.getClimate())
				.season(destination.getSeason())
				.seasonTags(listOrEmpty(destination.getSeasonTags()))
				.ambianceTags(listOrEmpty(destination.getAmbianceTags()))
				.vibes(resolveVibes(destination))
				.tags(listOrEmpty(destination.getTags()))
				.activities(listOrEmpty(destination.getActivities()))
				.images(imgs)
				.imageUrls(new ArrayList<>(imgs))
				.latitude(destination.getLatitude())
				.longitude(destination.getLongitude())
				.badges(listOrEmpty(destination.getBadges()))
				.popularityScore(destination.getPopularityScore())
				.visaRequired(destination.getVisaRequired())
				.promoted(destination.getPromoted())
				.build();
	}

	public Destination toEntity(DestinationCreateRequest request) {
		return Destination.builder()
				.name(request.getName())
				.country(request.getCountry())
				.continent(request.getContinent())
				.description(request.getDescription())
				.priceFrom(request.getPriceFrom())
				.averageHotelPrice(request.getAverageHotelPrice())
				.currency(request.getCurrency())
				.climate(request.getClimate())
				.season(request.getSeason())
				.seasonTags(copyList(request.getSeasonTags()))
				.ambianceTags(copyList(request.getAmbianceTags()))
				.vibes(copyList(request.getVibes()))
				.tags(copyList(request.getTags()))
				.activities(copyList(request.getActivities()))
				.images(copyList(request.getImages()))
				.imageUrls(copyList(request.getImageUrls()))
				.latitude(request.getLatitude())
				.longitude(request.getLongitude())
				.badges(copyList(request.getBadges()))
				.popularityScore(request.getPopularityScore())
				.visaRequired(request.getVisaRequired())
				.promoted(request.getPromoted())
				.build();
	}

	public void updateEntity(Destination destination, DestinationUpdateRequest request) {
		if (request.getName() != null) {
			destination.setName(request.getName());
		}
		if (request.getCountry() != null) {
			destination.setCountry(request.getCountry());
		}
		if (request.getContinent() != null) {
			destination.setContinent(request.getContinent());
		}
		if (request.getDescription() != null) {
			destination.setDescription(request.getDescription());
		}
		if (request.getPriceFrom() != null) {
			destination.setPriceFrom(request.getPriceFrom());
		}
		if (request.getAverageHotelPrice() != null) {
			destination.setAverageHotelPrice(request.getAverageHotelPrice());
		}
		if (request.getCurrency() != null) {
			destination.setCurrency(request.getCurrency());
		}
		if (request.getClimate() != null) {
			destination.setClimate(request.getClimate());
		}
		if (request.getSeason() != null) {
			destination.setSeason(request.getSeason());
		}
		if (request.getSeasonTags() != null) {
			destination.setSeasonTags(copyList(request.getSeasonTags()));
		}
		if (request.getAmbianceTags() != null) {
			destination.setAmbianceTags(copyList(request.getAmbianceTags()));
		}
		if (request.getVibes() != null) {
			destination.setVibes(copyList(request.getVibes()));
		}
		if (request.getTags() != null) {
			destination.setTags(copyList(request.getTags()));
		}
		if (request.getActivities() != null) {
			destination.setActivities(copyList(request.getActivities()));
		}
		if (request.getImages() != null) {
			destination.setImages(copyList(request.getImages()));
		}
		if (request.getImageUrls() != null) {
			destination.setImageUrls(copyList(request.getImageUrls()));
		}
		if (request.getLatitude() != null) {
			destination.setLatitude(request.getLatitude());
		}
		if (request.getLongitude() != null) {
			destination.setLongitude(request.getLongitude());
		}
		if (request.getBadges() != null) {
			destination.setBadges(copyList(request.getBadges()));
		}
		if (request.getPopularityScore() != null) {
			destination.setPopularityScore(request.getPopularityScore());
		}
		if (request.getVisaRequired() != null) {
			destination.setVisaRequired(request.getVisaRequired());
		}
		if (request.getPromoted() != null) {
			destination.setPromoted(request.getPromoted());
		}
	}

	private static ArrayList<String> copyList(List<String> source) {
		return new ArrayList<>(Objects.requireNonNullElse(source, Collections.emptyList()));
	}

	private static List<String> listOrEmpty(List<String> source) {
		return Objects.requireNonNullElseGet(source, Collections::emptyList);
	}

	private static List<String> mergedImages(Destination destination) {
		Set<String> merged = new LinkedHashSet<>();
		for (String imageUrl : listOrEmpty(destination.getImageUrls())) {
			if (imageUrl != null && !imageUrl.isBlank()) {
				merged.add(imageUrl.trim());
			}
		}
		for (String image : listOrEmpty(destination.getImages())) {
			if (image != null && !image.isBlank()) {
				merged.add(image.trim());
			}
		}
		return new ArrayList<>(merged);
	}
}
