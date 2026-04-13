package com.nexttrip.backend.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.nexttrip.backend.dto.response.FlightOfferResponse;
import com.nexttrip.backend.service.FlightSearchService;

/**
 * Amadeus Self-Service API (OAuth2 client credentials + flight offers).
 * Sans clés API, renvoie des offres factices pour le développement.
 */
@Service
public class FlightSearchServiceImpl implements FlightSearchService {

	private final RestClient restClient = RestClient.create();

	@Value("${nexttrip.amadeus.api-key:}")
	private String apiKey;

	@Value("${nexttrip.amadeus.api-secret:}")
	private String apiSecret;

	@Override
	public FlightOfferResponse search(String originIata, String destinationIata, String departureDate) {
		if (apiKey == null || apiKey.isBlank() || apiSecret == null || apiSecret.isBlank()) {
			return mockOffers(originIata, destinationIata, departureDate);
		}
		try {
			String tokenUrl = "https://test.api.amadeus.com/v1/security/oauth2/token";
			String form = "grant_type=client_credentials&client_id=" + apiKey + "&client_secret=" + apiSecret;
			@SuppressWarnings("rawtypes")
			Map tokenRes = restClient.post()
					.uri(tokenUrl)
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.body(form)
					.retrieve()
					.body(Map.class);
			if (tokenRes == null || !tokenRes.containsKey("access_token")) {
				return mockOffers(originIata, destinationIata, departureDate);
			}
			String token = String.valueOf(tokenRes.get("access_token"));
			String offersUrl = "https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode={o}&destinationLocationCode={d}&departureDate={date}&adults=1&max=3";
			restClient.get()
					.uri(offersUrl, originIata, destinationIata, departureDate)
					.headers(h -> h.setBearerAuth(token))
					.retrieve()
					.body(Map.class);
			return FlightOfferResponse.builder().mock(false).offers(List.of()).build();
		} catch (Exception e) {
			return mockOffers(originIata, destinationIata, departureDate);
		}
	}

	private static FlightOfferResponse mockOffers(String origin, String dest, String date) {
		return FlightOfferResponse.builder()
				.mock(true)
				.offers(List.of(
						FlightOfferResponse.Offer.builder()
								.carrier("Demo Air")
								.flightNumber("DM101")
								.price(BigDecimal.valueOf(289))
								.currency("EUR")
								.departureAt(date + "T08:00:00")
								.arrivalAt(date + "T11:30:00")
								.build(),
						FlightOfferResponse.Offer.builder()
								.carrier("Sample Airways")
								.flightNumber("SA404")
								.price(BigDecimal.valueOf(356))
								.currency("EUR")
								.departureAt(date + "T14:15:00")
								.arrivalAt(date + "T17:45:00")
								.build()))
				.build();
	}
}
