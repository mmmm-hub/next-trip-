package com.nexttrip.backend.service;

import com.nexttrip.backend.dto.response.WishlistResponse;

public interface WishlistService {

	WishlistResponse getWishlistByUserId(String userId);

	WishlistResponse addDestinationToWishlist(String userId, String destinationId);

	WishlistResponse removeDestinationFromWishlist(String userId, String destinationId);
}
