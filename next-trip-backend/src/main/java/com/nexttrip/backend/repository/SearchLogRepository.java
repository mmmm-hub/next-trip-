package com.nexttrip.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nexttrip.backend.model.SearchLog;

public interface SearchLogRepository extends MongoRepository<SearchLog, String> {

	java.util.List<SearchLog> findTop20ByUserIdOrderByCreatedAtDesc(String userId);
}
