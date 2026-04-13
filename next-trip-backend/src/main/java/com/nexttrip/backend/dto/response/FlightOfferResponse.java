package com.nexttrip.backend.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightOfferResponse {

	private boolean mock;
	private List<Offer> offers;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Offer {
		private String carrier;
		private String flightNumber;
		private BigDecimal price;
		private String currency;
		private String departureAt;
		private String arrivalAt;
	}
}
