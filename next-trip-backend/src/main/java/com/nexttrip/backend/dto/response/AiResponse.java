package com.nexttrip.backend.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiResponse {

	private String message;

	@Builder.Default
	private List<String> suggestions = new ArrayList<>();

	private boolean success;
}
