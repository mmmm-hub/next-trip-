package com.nexttrip.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexttrip.backend.dto.request.CompareRequest;
import com.nexttrip.backend.dto.response.CompareResponse;
import com.nexttrip.backend.service.CompareService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/compare")
@RequiredArgsConstructor
public class CompareController {

	private final CompareService compareService;

	@PostMapping
	public ResponseEntity<CompareResponse> compare(@Valid @RequestBody CompareRequest request) {
		return ResponseEntity.ok(compareService.compareDestinations(request));
	}
}
