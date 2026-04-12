package com.nexttrip.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexttrip.backend.dto.response.AdminStatsResponse;
import com.nexttrip.backend.dto.response.DestinationResponse;
import com.nexttrip.backend.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@GetMapping("/stats")
	public ResponseEntity<AdminStatsResponse> stats() {
		return ResponseEntity.ok(adminService.getStats());
	}

	@GetMapping("/top-destinations")
	public ResponseEntity<List<DestinationResponse>> topDestinations() {
		return ResponseEntity.ok(adminService.getTopDestinations());
	}

	@GetMapping("/search-analytics")
	public ResponseEntity<Map<String, Object>> searchAnalytics() {
		return ResponseEntity.ok(adminService.getSearchAnalytics());
	}
}
