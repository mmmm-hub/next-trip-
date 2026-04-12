package com.nexttrip.backend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nexttrip.backend.model.Wishlist;

public interface WishlistRepository extends MongoRepository<Wishlist, String> {

	Optional<Wishlist> findByUserId(String userId);
}
