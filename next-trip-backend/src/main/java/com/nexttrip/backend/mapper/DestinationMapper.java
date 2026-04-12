package com.nexttrip.backend.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.nexttrip.backend.dto.request.DestinationCreateRequest;
import com.nexttrip.backend.dto.request.DestinationUpdateRequest;
import com.nexttrip.backend.dto.response.DestinationDetailsResponse;
import com.nexttrip.backend.dto.response.DestinationResponse;
import com.nexttrip.backend.model.Destination;

@Component
public class DestinationMapper {

	public DestinationResponse toResponse(Destination destination) {
		return DestinationResponse.builder()
				.id(destination.getId())
				.name(destination.getName())
				.country(destination.getCountry())
				.continent(destination.getContinent())
				.priceFrom(destination.getPriceFrom())
				.currency(destination.getCurrency())
				.climate(destination.getClimate())
				.images(listOrEmpty(destination.getImages()))
				.badges(listOrEmpty(destination.getBadges()))
				.popularityScore(destination.getPopularityScore())
				.visaRequired(destination.getVisaRequired())
				.promoted(destination.getPromoted())
				.build();
	}

	/**
	 * Cartographie complète d'une destination (sans agrégats avis — renseignés côté service si besoin).
	 */
	public DestinationDetailsResponse toDetailsResponse(Destination destination) {
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
				.seasonTags(listOrEmpty(destination.getSeasonTags()))
				.ambianceTags(listOrEmpty(destination.getAmbianceTags()))
				.activities(listOrEmpty(destination.getActivities()))
				.images(listOrEmpty(destination.getImages()))
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
				.seasonTags(copyList(request.getSeasonTags()))
				.ambianceTags(copyList(request.getAmbianceTags()))
				.activities(copyList(request.getActivities()))
				.images(copyList(request.getImages()))
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
		if (request.getSeasonTags() != null) {
			destination.setSeasonTags(copyList(request.getSeasonTags()));
		}
		if (request.getAmbianceTags() != null) {
			destination.setAmbianceTags(copyList(request.getAmbianceTags()));
		}
		if (request.getActivities() != null) {
			destination.setActivities(copyList(request.getActivities()));
		}
		if (request.getImages() != null) {
			destination.setImages(copyList(request.getImages()));
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
}
