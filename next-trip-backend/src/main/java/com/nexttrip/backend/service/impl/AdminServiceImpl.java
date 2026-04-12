package com.nexttrip.backend.service.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.nexttrip.backend.dto.response.AdminStatsResponse;
import com.nexttrip.backend.dto.response.DestinationResponse;
import com.nexttrip.backend.mapper.DestinationMapper;
import com.nexttrip.backend.model.Destination;
import com.nexttrip.backend.repository.DestinationRepository;
import com.nexttrip.backend.repository.ReviewRepository;
import com.nexttrip.backend.repository.SearchLogRepository;
import com.nexttrip.backend.repository.UserRepository;
import com.nexttrip.backend.repository.WishlistRepository;
import com.nexttrip.backend.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private static final int TOP_LIMIT = 5;

	private final DestinationRepository destinationRepository;
	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final WishlistRepository wishlistRepository;
	private final SearchLogRepository searchLogRepository;
	private final DestinationMapper destinationMapper;

	@Override
	public AdminStatsResponse getStats() {
		return AdminStatsResponse.builder()
				.totalUsers(userRepository.count())
				.totalDestinations(destinationRepository.count())
				.totalReviews(reviewRepository.count())
				.totalWishlists(wishlistRepository.count())
				.totalSearchLogs(searchLogRepository.count())
				.build();
	}

	@Override
	public List<DestinationResponse> getTopDestinations() {
		return destinationRepository.findAll().stream()
				.sorted(Comparator.comparing(
						Destination::getPopularityScore,
						Comparator.nullsLast(Double::compareTo)).reversed())
				.limit(TOP_LIMIT)
				.map(destinationMapper::toResponse)
				.toList();
	}

	@Override
	public Map<String, Object> getSearchAnalytics() {
		Map<String, Object> map = new HashMap<>();
		map.put("totalLoggedSearches", searchLogRepository.count());
		map.put("topFilterKeysMock", List.of("country", "continent", "climate", "maxPrice"));
		map.put("note", "Données agrégées simplifiées pour le tableau de bord (mock partiel).");
		return map;
	}
}
