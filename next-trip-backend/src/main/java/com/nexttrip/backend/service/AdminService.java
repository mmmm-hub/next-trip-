package com.nexttrip.backend.service;

import java.util.List;
import java.util.Map;

import com.nexttrip.backend.dto.response.AdminStatsResponse;
import com.nexttrip.backend.dto.response.DestinationResponse;

public interface AdminService {

	AdminStatsResponse getStats();

	List<DestinationResponse> getTopDestinations();

	Map<String, Object> getSearchAnalytics();
}
