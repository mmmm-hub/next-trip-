package com.nexttrip.backend.service;

import com.nexttrip.backend.dto.request.CompareRequest;
import com.nexttrip.backend.dto.response.CompareResponse;

public interface CompareService {

	CompareResponse compareDestinations(CompareRequest request);
}
