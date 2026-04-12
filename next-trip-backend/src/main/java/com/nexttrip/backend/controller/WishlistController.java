package com.nexttrip.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexttrip.backend.dto.response.WishlistResponse;
import com.nexttrip.backend.service.WishlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

	private final WishlistService wishlistService;

	@GetMapping("/{userId}")
	public ResponseEntity<WishlistResponse> getByUser(@PathVariable String userId) {
		return ResponseEntity.ok(wishlistService.getWishlistByUserId(userId));
	}

	@PostMapping("/{userId}/{destinationId}")
	public ResponseEntity<WishlistResponse> add(
			@PathVariable String userId,
			@PathVariable String destinationId) {
		WishlistResponse body = wishlistService.addDestinationToWishlist(userId, destinationId);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@DeleteMapping("/{userId}/{destinationId}")
	public ResponseEntity<WishlistResponse> remove(
			@PathVariable String userId,
			@PathVariable String destinationId) {
		return ResponseEntity.ok(wishlistService.removeDestinationFromWishlist(userId, destinationId));
	}
}
