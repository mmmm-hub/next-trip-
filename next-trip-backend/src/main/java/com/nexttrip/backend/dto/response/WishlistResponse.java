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
public class WishlistResponse {

	private String id;
	private String userId;

	@Builder.Default
	private List<DestinationResponse> destinations = new ArrayList<>();
}
