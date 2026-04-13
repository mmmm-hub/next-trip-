package com.nexttrip.backend.service;

import com.nexttrip.backend.dto.response.FlightOfferResponse;

public interface FlightSearchService {

	FlightOfferResponse search(String originIata, String destinationIata, String departureDate);
}
