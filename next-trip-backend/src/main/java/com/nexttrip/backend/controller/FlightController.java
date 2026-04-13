package com.nexttrip.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexttrip.backend.dto.response.FlightOfferResponse;
import com.nexttrip.backend.service.FlightSearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

	private final FlightSearchService flightSearchService;

	@GetMapping("/search")
	public ResponseEntity<FlightOfferResponse> search(
			@RequestParam String origin,
			@RequestParam String destination,
			@RequestParam String departureDate) {
		return ResponseEntity.ok(flightSearchService.search(origin, destination, departureDate));
	}
}
