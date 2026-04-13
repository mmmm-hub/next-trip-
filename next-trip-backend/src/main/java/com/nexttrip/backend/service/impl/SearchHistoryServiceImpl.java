package com.nexttrip.backend.service.impl;

import java.time.Instant;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.nexttrip.backend.dto.request.SearchHistoryRequest;
import com.nexttrip.backend.model.SearchLog;
import com.nexttrip.backend.repository.SearchLogRepository;
import com.nexttrip.backend.service.SearchHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {

	private final SearchLogRepository searchLogRepository;

	@Override
	public void logSearch(String userEmail, SearchHistoryRequest request) {
		SearchLog log = SearchLog.builder()
				.userId(userEmail)
				.filters(request.getFilters() != null ? request.getFilters() : new HashMap<>())
				.resultCount(request.getResultCount())
				.createdAt(Instant.now())
				.build();
		searchLogRepository.save(log);
	}
}
