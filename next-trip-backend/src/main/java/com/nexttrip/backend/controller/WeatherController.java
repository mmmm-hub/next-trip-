package com.nexttrip.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexttrip.backend.dto.response.WeatherResponse;
import com.nexttrip.backend.service.WeatherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

	private final WeatherService weatherService;

	@GetMapping("/destination/{destinationId}")
	public ResponseEntity<WeatherResponse> forDestination(@PathVariable String destinationId) {
		return ResponseEntity.ok(weatherService.getWeatherForDestination(destinationId));
	}
}
