package com.nexttrip.backend.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nexttrip.backend.dto.request.RecommendationRequest;
import com.nexttrip.backend.dto.response.RecommendationItemResponse;
import com.nexttrip.backend.model.Destination;
import com.nexttrip.backend.model.SearchLog;
import com.nexttrip.backend.model.Wishlist;
import com.nexttrip.backend.repository.DestinationRepository;
import com.nexttrip.backend.repository.SearchLogRepository;
import com.nexttrip.backend.repository.WishlistRepository;
import com.nexttrip.backend.service.RecommendationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

	private final DestinationRepository destinationRepository;
	private final WishlistRepository wishlistRepository;
	private final SearchLogRepository searchLogRepository;

	@Override
	public List<RecommendationItemResponse> recommend(String userEmail, RecommendationRequest request) {
		List<Destination> all = destinationRepository.findAll();
		if (all.isEmpty()) {
			return List.of();
		}

		Set<String> wishlistDestIds = loadWishlistDestinationIds(userEmail);
		List<SearchLog> searchHistory = loadRecentHistory(userEmail);

		int limit = resolveLimit(request.getLimit());
		return all.stream()
				.map(destination -> scoreDestination(destination, all, request, wishlistDestIds, searchHistory))
				.sorted(Comparator.comparing(RecommendationItemResponse::getScore).reversed())
				.limit(limit)
				.toList();
	}

	private RecommendationItemResponse scoreDestination(
			Destination destination,
			List<Destination> allDestinations,
			RecommendationRequest request,
			Set<String> wishlistDestinationIds,
			List<SearchLog> history) {
		double score = 0.0;
		List<String> reasons = new ArrayList<>();

		score += scorePopularity(destination, reasons);
		score += scoreBudget(destination, request.getMinBudget(), request.getMaxBudget(), reasons);
		score += scoreClimate(destination, request.getClimate(), reasons);
		score += scoreSeason(destination, request.getSeason(), reasons);
		score += scoreAmbiance(destination, request.getAmbianceTags(), reasons);
		score += scoreWishlistAffinity(destination, allDestinations, wishlistDestinationIds, reasons);
		score += scoreHistoryAffinity(destination, history, reasons);

		return RecommendationItemResponse.builder()
				.destinationId(destination.getId())
				.score(Math.round(score * 100.0) / 100.0)
				.reasons(reasons)
				.build();
	}

	private static double scorePopularity(Destination destination, List<String> reasons) {
		double popularity = destination.getPopularityScore() != null ? destination.getPopularityScore() : 0.0;
		double normalized = Math.max(0.0, Math.min(popularity, 100.0));
		double points = normalized * 0.20;
		if (points > 12.0) {
			reasons.add("Popular destination");
		}
		return points;
	}

	private static double scoreBudget(
			Destination destination,
			Double minBudget,
			Double maxBudget,
			List<String> reasons) {
		if (destination.getPriceFrom() == null || (minBudget == null && maxBudget == null)) {
			return 0.0;
		}
		BigDecimal price = destination.getPriceFrom();
		double p = price.doubleValue();
		double points = 0.0;

		boolean minOk = minBudget == null || p >= minBudget;
		boolean maxOk = maxBudget == null || p <= maxBudget;
		if (minOk && maxOk) {
			points += 30.0;
			reasons.add("Matches your budget");
		} else {
			double gap = 0.0;
			if (!minOk && minBudget != null) {
				gap = minBudget - p;
			} else if (!maxOk && maxBudget != null) {
				gap = p - maxBudget;
			}
			points += Math.max(0.0, 15.0 - (gap / 100.0));
		}
		return points;
	}

	private static double scoreClimate(Destination destination, String requestedClimate, List<String> reasons) {
		if (requestedClimate == null || requestedClimate.isBlank() || destination.getClimate() == null) {
			return 0.0;
		}
		String desired = normalize(requestedClimate);
		String actual = normalize(destination.getClimate());
		if (actual.contains(desired) || desired.contains(actual)) {
			reasons.add("Climate fits your preference");
			return 12.0;
		}
		return 0.0;
	}

	private static double scoreSeason(Destination destination, String season, List<String> reasons) {
		if (season == null || season.isBlank() || destination.getSeasonTags() == null) {
			return 0.0;
		}
		String desired = normalize(season);
		boolean match = destination.getSeasonTags().stream()
				.filter(Objects::nonNull)
				.map(RecommendationServiceImpl::normalize)
				.anyMatch(tag -> tag.contains(desired) || desired.contains(tag));
		if (match) {
			reasons.add("Great season match");
			return 10.0;
		}
		return 0.0;
	}

	private static double scoreAmbiance(Destination destination, List<String> requestedAmbiances, List<String> reasons) {
		if (requestedAmbiances == null || requestedAmbiances.isEmpty() || destination.getAmbianceTags() == null) {
			return 0.0;
		}
		Set<String> desired = requestedAmbiances.stream()
				.filter(Objects::nonNull)
				.map(RecommendationServiceImpl::normalize)
				.collect(Collectors.toSet());
		long overlap = destination.getAmbianceTags().stream()
				.filter(Objects::nonNull)
				.map(RecommendationServiceImpl::normalize)
				.filter(desired::contains)
				.count();
		if (overlap > 0) {
			reasons.add("Ambiance aligns with your mood");
		}
		return Math.min(20.0, overlap * 8.0);
	}

	private static double scoreWishlistAffinity(
			Destination destination,
			List<Destination> allDestinations,
			Set<String> wishlistDestinationIds,
			List<String> reasons) {
		if (wishlistDestinationIds.isEmpty()) {
			return 0.0;
		}
		Set<String> wishlistCountries = new HashSet<>();
		Set<String> wishlistContinents = new HashSet<>();
		Set<String> wishlistClimates = new HashSet<>();
		Set<String> wishlistAmbiances = new HashSet<>();

		for (Destination wish : allDestinations) {
			if (!wishlistDestinationIds.contains(wish.getId())) {
				continue;
			}
			if (wish.getCountry() != null) {
				wishlistCountries.add(normalize(wish.getCountry()));
			}
			if (wish.getContinent() != null) {
				wishlistContinents.add(normalize(wish.getContinent()));
			}
			if (wish.getClimate() != null) {
				wishlistClimates.add(normalize(wish.getClimate()));
			}
			if (wish.getAmbianceTags() != null) {
				for (String ambiance : wish.getAmbianceTags()) {
					if (ambiance != null) {
						wishlistAmbiances.add(normalize(ambiance));
					}
				}
			}
		}

		double score = 0.0;
		if (destination.getCountry() != null && wishlistCountries.contains(normalize(destination.getCountry()))) {
			score += 8.0;
		}
		if (destination.getContinent() != null && wishlistContinents.contains(normalize(destination.getContinent()))) {
			score += 5.0;
		}
		if (destination.getClimate() != null && wishlistClimates.contains(normalize(destination.getClimate()))) {
			score += 5.0;
		}
		if (destination.getAmbianceTags() != null) {
			long overlap = destination.getAmbianceTags().stream()
					.filter(Objects::nonNull)
					.map(RecommendationServiceImpl::normalize)
					.filter(wishlistAmbiances::contains)
					.count();
			score += Math.min(5.0, overlap * 2.0);
		}
		if (score > 0.0) {
			reasons.add("Similar to your wishlist taste");
		}
		return Math.min(score, 15.0);
	}

	private static double scoreHistoryAffinity(Destination destination, List<SearchLog> history, List<String> reasons) {
		if (history.isEmpty()) {
			return 0.0;
		}

		double score = 0.0;
		for (SearchLog log : history) {
			Map<String, Object> filters = log.getFilters();
			if (filters == null || filters.isEmpty()) {
				continue;
			}
			score += scoreSingleHistory(filters, destination);
		}

		double capped = Math.min(18.0, score);
		if (capped >= 6.0) {
			reasons.add("Matches your recent searches");
		}
		return capped;
	}

	private static double scoreSingleHistory(Map<String, Object> filters, Destination destination) {
		double score = 0.0;
		String country = asString(filters.get("country"));
		String continent = asString(filters.get("continent"));
		String climate = asString(filters.get("climate"));
		String ambiance = asString(filters.get("ambiance"));
		String season = asString(filters.get("season"));
		Double minPrice = asDouble(filters.get("minPrice"));
		Double maxPrice = asDouble(filters.get("maxPrice"));

		if (country != null && destination.getCountry() != null
				&& normalize(destination.getCountry()).contains(normalize(country))) {
			score += 1.6;
		}
		if (continent != null && destination.getContinent() != null
				&& normalize(destination.getContinent()).contains(normalize(continent))) {
			score += 1.2;
		}
		if (climate != null && destination.getClimate() != null
				&& normalize(destination.getClimate()).contains(normalize(climate))) {
			score += 1.2;
		}
		if (season != null && destination.getSeasonTags() != null && destination.getSeasonTags().stream()
				.filter(Objects::nonNull)
				.map(RecommendationServiceImpl::normalize)
				.anyMatch(tag -> tag.contains(normalize(season)))) {
			score += 1.0;
		}
		if (ambiance != null && destination.getAmbianceTags() != null && destination.getAmbianceTags().stream()
				.filter(Objects::nonNull)
				.map(RecommendationServiceImpl::normalize)
				.anyMatch(tag -> tag.contains(normalize(ambiance)))) {
			score += 1.2;
		}
		if (destination.getPriceFrom() != null && (minPrice != null || maxPrice != null)) {
			double p = destination.getPriceFrom().doubleValue();
			if ((minPrice == null || p >= minPrice) && (maxPrice == null || p <= maxPrice)) {
				score += 1.8;
			}
		}
		return score;
	}

	private Set<String> loadWishlistDestinationIds(String userEmail) {
		if (userEmail == null || userEmail.isBlank()) {
			return Set.of();
		}
		return wishlistRepository.findByUserId(userEmail)
				.map(Wishlist::getDestinationIds)
				.orElse(List.of())
				.stream()
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	private List<SearchLog> loadRecentHistory(String userEmail) {
		if (userEmail == null || userEmail.isBlank()) {
			return List.of();
		}
		return searchLogRepository.findTop20ByUserIdOrderByCreatedAtDesc(userEmail);
	}

	private static int resolveLimit(Integer limit) {
		if (limit == null) {
			return 6;
		}
		return Math.max(1, Math.min(limit, 20));
	}

	private static String normalize(String value) {
		return value.trim().toLowerCase(Locale.ROOT);
	}

	private static String asString(Object value) {
		if (value == null) {
			return null;
		}
		String out = String.valueOf(value).trim();
		return out.isEmpty() ? null : out;
	}

	private static Double asDouble(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Number num) {
			return num.doubleValue();
		}
		try {
			return Double.parseDouble(String.valueOf(value));
		} catch (NumberFormatException ex) {
			return null;
		}
	}
}
