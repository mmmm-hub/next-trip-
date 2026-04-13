package com.nexttrip.backend.dto.request;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryRequest {

	@Builder.Default
	private Map<String, Object> filters = new HashMap<>();

	private Integer resultCount;
}
