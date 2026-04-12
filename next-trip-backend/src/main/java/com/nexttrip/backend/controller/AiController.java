package com.nexttrip.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexttrip.backend.dto.request.AiRequest;
import com.nexttrip.backend.dto.response.AiResponse;
import com.nexttrip.backend.service.AiService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

	private final AiService aiService;

	@PostMapping("/ask")
	public ResponseEntity<AiResponse> ask(@Valid @RequestBody AiRequest request) {
		return ResponseEntity.ok(aiService.askQuestion(request));
	}

	@PostMapping("/plan")
	public ResponseEntity<AiResponse> plan(@Valid @RequestBody AiRequest request) {
		return ResponseEntity.ok(aiService.generatePlan(request));
	}
}
