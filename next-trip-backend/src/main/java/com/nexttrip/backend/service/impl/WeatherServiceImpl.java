package com.nexttrip.backend.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.nexttrip.backend.dto.response.WeatherResponse;
import com.nexttrip.backend.exception.ResourceNotFoundException;
import com.nexttrip.backend.model.Destination;
import com.nexttrip.backend.repository.DestinationRepository;
import com.nexttrip.backend.service.WeatherService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

	private final DestinationRepository destinationRepository;
	private final RestClient restClient = RestClient.create();

	@Value("${nexttrip.openweather.api-key:}")
	private String apiKey;

	@Override
	public WeatherResponse getWeatherForDestination(String destinationId) {
		Destination d = destinationRepository.findById(destinationId)
				.orElseThrow(() -> new ResourceNotFoundException("Destination", destinationId));
		if (d.getLatitude() == null || d.getLongitude() == null) {
			return WeatherResponse.builder()
					.summary("Coordonnées manquantes pour la météo")
					.temperatureC(0)
					.humidityPercent(0)
					.windSpeedMs(0)
					.mock(true)
					.build();
		}
		if (apiKey == null || apiKey.isBlank()) {
			return mockWeather(d.getName());
		}
		try {
			String url = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={key}&units=metric";
			Map<?, ?> body = restClient.get()
					.uri(url, d.getLatitude(), d.getLongitude(), apiKey)
					.retrieve()
					.body(Map.class);
			if (body == null) {
				return mockWeather(d.getName());
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> main = (Map<String, Object>) body.get("main");
			@SuppressWarnings("unchecked")
			Map<String, Object> wind = (Map<String, Object>) body.get("wind");
			@SuppressWarnings("unchecked")
			Map<String, Object> weather0 = ((java.util.List<Map<String, Object>>) body.get("weather")).get(0);
			double temp = main != null && main.get("temp") instanceof Number n ? n.doubleValue() : 0;
			int hum = main != null && main.get("humidity") instanceof Number n ? n.intValue() : 0;
			double w = wind != null && wind.get("speed") instanceof Number n ? n.doubleValue() : 0;
			String desc = weather0 != null ? String.valueOf(weather0.get("description")) : "n/a";
			return WeatherResponse.builder()
					.summary(desc)
					.temperatureC(temp)
					.humidityPercent(hum)
					.windSpeedMs(w)
					.mock(false)
					.build();
		} catch (Exception e) {
			return mockWeather(d.getName());
		}
	}

	private static WeatherResponse mockWeather(String name) {
		return WeatherResponse.builder()
				.summary("Conditions typiques (démo) — " + name)
				.temperatureC(21)
				.humidityPercent(55)
				.windSpeedMs(3.2)
				.mock(true)
				.build();
	}
}
