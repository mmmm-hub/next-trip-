package com.nexttrip.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {

	private String summary;
	private double temperatureC;
	private int humidityPercent;
	private double windSpeedMs;
	private boolean mock;
}
