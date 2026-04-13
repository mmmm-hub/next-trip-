package com.nexttrip.backend.service;

import com.nexttrip.backend.dto.request.SearchHistoryRequest;

public interface SearchHistoryService {

	void logSearch(String userEmail, SearchHistoryRequest request);
}
