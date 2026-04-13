package com.nexttrip.backend.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.nexttrip.backend.dto.request.DestinationCreateRequest;
import com.nexttrip.backend.dto.request.DestinationUpdateRequest;
import com.nexttrip.backend.dto.response.DestinationDetailsResponse;
import com.nexttrip.backend.dto.response.DestinationResponse;
import com.nexttrip.backend.exception.ResourceNotFoundException;
import com.nexttrip.backend.mapper.DestinationMapper;
import com.nexttrip.backend.model.Destination;
import com.nexttrip.backend.model.Review;
import com.nexttrip.backend.repository.DestinationRepository;
import com.nexttrip.backend.repository.ReviewRepository;
import com.nexttrip.backend.service.DestinationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {

	private final DestinationRepository destinationRepository;
	private final ReviewRepository reviewRepository;
	private final DestinationMapper destinationMapper;

	@Override
	public List<DestinationResponse> getAllDestinations() {
		return CompletableFuture.supplyAsync(() -> {
					List<DestinationResponse> list = destinationRepository.findAll().stream()
							.map(destinationMapper::toResponse)
							.toList();
					enrichReviewStats(list);
					return list;
				})
				.completeOnTimeout(buildMockDestinations(), 800, TimeUnit.MILLISECONDS)
				.exceptionally(ex -> {
					List<DestinationResponse> mock = buildMockDestinations();
					enrichReviewStats(mock);
					return mock;
				})
				.join();
	}

	private void enrichReviewStats(List<DestinationResponse> responses) {
		if (responses == null || responses.isEmpty()) {
			return;
		}
		List<Review> all;
		try {
			all = reviewRepository.findAll();
		} catch (Exception ex) {
			return;
		}
		Map<String, List<Integer>> byDestination = all.stream()
				.filter(r -> r.getDestinationId() != null && r.getRating() != null)
				.collect(Collectors.groupingBy(
						Review::getDestinationId,
						Collectors.mapping(Review::getRating, Collectors.toList())));
		for (DestinationResponse r : responses) {
			List<Integer> ratings = byDestination.getOrDefault(r.getId(), List.of());
			if (ratings.isEmpty()) {
				continue;
			}
			r.setReviews(ratings.size());
			r.setRating(ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0));
		}
	}

	@Override
	public DestinationDetailsResponse getDestinationById(String id) {
		Destination destination = destinationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Destination", id));
		DestinationDetailsResponse response = destinationMapper.toDetailsResponse(destination);
		List<Review> reviews = reviewRepository.findByDestinationIdOrderByCreatedAtDesc(id);
		double avg = reviews.stream()
				.mapToInt(Review::getRating)
				.average()
				.orElse(0.0);
		response.setAverageRating(reviews.isEmpty() ? null : avg);
		response.setReviewCount((long) reviews.size());
		return response;
	}

	@Override
	public DestinationResponse createDestination(DestinationCreateRequest request) {
		Destination entity = destinationMapper.toEntity(request);
		Destination saved = destinationRepository.save(entity);
		return destinationMapper.toResponse(saved);
	}

	@Override
	public DestinationResponse updateDestination(String id, DestinationUpdateRequest request) {
		Destination destination = destinationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Destination", id));
		destinationMapper.updateEntity(destination, request);
		Destination saved = destinationRepository.save(destination);
		return destinationMapper.toResponse(saved);
	}

	@Override
	public void deleteDestination(String id) {
		if (!destinationRepository.existsById(id)) {
			throw new ResourceNotFoundException("Destination", id);
		}
		destinationRepository.deleteById(id);
	}

	@Override
	public List<DestinationResponse> searchDestinations(
			String country,
			String continent,
			String climate,
			String ambiance,
			Double minPrice,
			Double maxPrice,
			String sortBy) {
		Stream<Destination> stream = destinationRepository.findAll().stream();

		if (country != null && !country.isBlank()) {
			String c = country.trim().toLowerCase(Locale.ROOT);
			stream = stream.filter(d -> d.getCountry() != null
					&& d.getCountry().toLowerCase(Locale.ROOT).contains(c));
		}
		if (continent != null && !continent.isBlank()) {
			String ct = continent.trim().toLowerCase(Locale.ROOT);
			stream = stream.filter(d -> d.getContinent() != null
					&& d.getContinent().toLowerCase(Locale.ROOT).contains(ct));
		}
		if (climate != null && !climate.isBlank()) {
			String cl = climate.trim().toLowerCase(Locale.ROOT);
			stream = stream.filter(d -> d.getClimate() != null
					&& d.getClimate().toLowerCase(Locale.ROOT).contains(cl));
		}
		if (ambiance != null && !ambiance.isBlank()) {
			String am = ambiance.trim().toLowerCase(Locale.ROOT);
			stream = stream.filter(d -> matchesAmbiance(d, am));
		}
		if (minPrice != null) {
			BigDecimal min = BigDecimal.valueOf(minPrice);
			stream = stream.filter(d -> d.getPriceFrom() != null && d.getPriceFrom().compareTo(min) >= 0);
		}
		if (maxPrice != null) {
			BigDecimal max = BigDecimal.valueOf(maxPrice);
			stream = stream.filter(d -> d.getPriceFrom() != null && d.getPriceFrom().compareTo(max) <= 0);
		}

		Comparator<Destination> comparator = resolveComparator(sortBy);
		List<DestinationResponse> list = stream
				.sorted(comparator)
				.map(destinationMapper::toResponse)
				.toList();
		enrichReviewStats(list);
		return list;
	}

	private static boolean matchesAmbiance(Destination d, String needle) {
		return Stream.of(d.getAmbianceTags(), d.getVibes(), d.getTags())
				.filter(Objects::nonNull)
				.flatMap(List::stream)
				.filter(Objects::nonNull)
				.map(s -> s.toLowerCase(Locale.ROOT))
				.anyMatch(tag -> tag.contains(needle));
	}

	private static Comparator<Destination> resolveComparator(String sortBy) {
		if (sortBy == null || sortBy.isBlank()) {
			return Comparator.comparing(Destination::getName, Comparator.nullsLast(String::compareToIgnoreCase));
		}
		return switch (sortBy.trim().toLowerCase(Locale.ROOT)) {
			case "price", "pricefrom" -> Comparator.comparing(
					Destination::getPriceFrom,
					Comparator.nullsLast(BigDecimal::compareTo));
			case "popularity", "popularityscore" -> Comparator.comparing(
					Destination::getPopularityScore,
					Comparator.nullsLast(Double::compareTo));
			default -> Comparator.comparing(Destination::getName, Comparator.nullsLast(String::compareToIgnoreCase));
		};
	}

	private static List<DestinationResponse> buildMockDestinations() {
		List<DestinationResponse> mock = new ArrayList<>();
		mock.add(DestinationResponse.builder()
				.id("mock-paris")
				.name("Paris")
				.country("France")
				.continent("Europe")
				.description("Capitale culturelle.")
				.priceFrom(BigDecimal.valueOf(1200))
				.currency("EUR")
				.climate("Tempéré")
				.season("automne")
				.seasonTags(List.of("printemps", "automne"))
				.vibes(List.of("Culturel", "Romantique"))
				.tags(List.of("city-break"))
				.activities(List.of("Musées", "Gastronomie"))
				.images(List.of(
						"https://images.unsplash.com/photo-1502602898657-3e91760cbb34?auto=format&fit=crop&w=800&q=70"))
				.imageUrls(List.of(
						"https://images.unsplash.com/photo-1502602898657-3e91760cbb34?auto=format&fit=crop&w=800&q=70"))
				.image("https://images.unsplash.com/photo-1502602898657-3e91760cbb34?auto=format&fit=crop&w=800&q=70")
				.badges(List.of())
				.popularityScore(90.0)
				.rating(4.6)
				.reviews(210)
				.latitude(48.8566)
				.longitude(2.3522)
				.build());
		mock.add(DestinationResponse.builder()
				.id("mock-rome")
				.name("Rome")
				.country("Italy")
				.continent("Europe")
				.description("Histoire et dolce vita.")
				.priceFrom(BigDecimal.valueOf(990))
				.currency("EUR")
				.climate("Méditerranéen")
				.season("été")
				.seasonTags(List.of("printemps", "été"))
				.vibes(List.of("Culturel", "Gastronomie"))
				.tags(List.of("patrimoine"))
				.activities(List.of("Antiquité", "Cuisine"))
				.images(List.of(
						"https://images.unsplash.com/photo-1552832230-0197db3ec960?auto=format&fit=crop&w=800&q=70"))
				.imageUrls(List.of(
						"https://images.unsplash.com/photo-1552832230-0197db3ec960?auto=format&fit=crop&w=800&q=70"))
				.image("https://images.unsplash.com/photo-1552832230-0197db3ec960?auto=format&fit=crop&w=800&q=70")
				.badges(List.of())
				.popularityScore(87.0)
				.rating(4.5)
				.reviews(185)
				.latitude(41.9028)
				.longitude(12.4964)
				.build());
		return mock;
	}
}
