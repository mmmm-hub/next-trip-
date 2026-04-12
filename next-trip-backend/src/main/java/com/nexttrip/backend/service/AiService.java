package com.nexttrip.backend.service;

import com.nexttrip.backend.dto.request.AiRequest;
import com.nexttrip.backend.dto.response.AiResponse;

public interface AiService {

	AiResponse askQuestion(AiRequest request);

	AiResponse generatePlan(AiRequest request);
}
