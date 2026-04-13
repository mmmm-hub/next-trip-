package com.nexttrip.backend.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nexttrip.backend.dto.response.DestinationResponse;
import com.nexttrip.backend.dto.response.WishlistResponse;
import org.springframework.security.access.AccessDeniedException;

import com.nexttrip.backend.exception.ResourceNotFoundException;
import com.nexttrip.backend.mapper.DestinationMapper;
import com.nexttrip.backend.model.Destination;
import com.nexttrip.backend.model.Wishlist;
import com.nexttrip.backend.repository.DestinationRepository;
import com.nexttrip.backend.repository.WishlistRepository;
import com.nexttrip.backend.security.SecurityUtils;
import com.nexttrip.backend.service.WishlistService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

	private final WishlistRepository wishlistRepository;
	private final DestinationRepository destinationRepository;
	private final DestinationMapper destinationMapper;

	@Override
	public WishlistResponse getWishlistByUserId(String userId) {
		assertOwner(userId);
		Wishlist wishlist = wishlistRepository.findByUserId(userId)
				.orElseGet(() -> Wishlist.builder().userId(userId).destinationIds(new ArrayList<>()).build());
		return toResponse(wishlist);
	}

	@Override
	public WishlistResponse addDestinationToWishlist(String userId, String destinationId) {
		assertOwner(userId);
		if (!destinationRepository.existsById(destinationId)) {
			throw new ResourceNotFoundException("Destination", destinationId);
		}
		Wishlist wishlist = wishlistRepository.findByUserId(userId)
				.orElseGet(() -> Wishlist.builder().userId(userId).destinationIds(new ArrayList<>()).build());
		List<String> ids = wishlist.getDestinationIds();
		if (ids == null) {
			ids = new ArrayList<>();
			wishlist.setDestinationIds(ids);
		}
		if (!ids.contains(destinationId)) {
			ids.add(destinationId);
		}
		Wishlist saved = wishlistRepository.save(wishlist);
		return toResponse(saved);
	}

	@Override
	public WishlistResponse removeDestinationFromWishlist(String userId, String destinationId) {
		assertOwner(userId);
		Optional<Wishlist> opt = wishlistRepository.findByUserId(userId);
		if (opt.isEmpty()) {
			throw new ResourceNotFoundException("Wishlist for user not found: " + userId);
		}
		Wishlist wishlist = opt.get();
		List<String> ids = wishlist.getDestinationIds();
		if (ids != null) {
			ids.removeIf(destinationId::equals);
		}
		Wishlist saved = wishlistRepository.save(wishlist);
		return toResponse(saved);
	}

	private static void assertOwner(String userId) {
		String email = SecurityUtils.currentUserEmail();
		if (email == null || !email.equalsIgnoreCase(userId)) {
			throw new AccessDeniedException("Wishlist accessible uniquement pour le compte connecté");
		}
	}

	private WishlistResponse toResponse(Wishlist wishlist) {
		List<DestinationResponse> destinations = new ArrayList<>();
		if (wishlist.getDestinationIds() != null) {
			for (String destId : wishlist.getDestinationIds()) {
				Optional<Destination> dest = destinationRepository.findById(destId);
				dest.map(destinationMapper::toResponse).ifPresent(destinations::add);
			}
		}
		return WishlistResponse.builder()
				.id(wishlist.getId())
				.userId(wishlist.getUserId())
				.destinations(destinations)
				.build();
	}
}
