package com.nexttrip.backend.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "search_logs")
public class SearchLog {

	@Id
	private String id;

	@Builder.Default
	private Map<String, Object> filters = new HashMap<>();

	private Integer resultCount;

	private Instant createdAt;
}
