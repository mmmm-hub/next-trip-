package com.nexttrip.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexttrip.backend.dto.request.SearchHistoryRequest;
import com.nexttrip.backend.security.SecurityUtils;
import com.nexttrip.backend.service.SearchHistoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search-history")
@RequiredArgsConstructor
public class SearchHistoryController {

	private final SearchHistoryService searchHistoryService;

	@PostMapping
	public ResponseEntity<Void> log(@Valid @RequestBody SearchHistoryRequest request) {
		String email = SecurityUtils.currentUserEmail();
		if (email == null) {
			return ResponseEntity.status(401).build();
		}
		searchHistoryService.logSearch(email, request);
		return ResponseEntity.accepted().build();
	}
}
