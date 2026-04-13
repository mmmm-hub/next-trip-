package com.nexttrip.backend.service;

import com.nexttrip.backend.dto.response.WeatherResponse;

public interface WeatherService {

	WeatherResponse getWeatherForDestination(String destinationId);
}
