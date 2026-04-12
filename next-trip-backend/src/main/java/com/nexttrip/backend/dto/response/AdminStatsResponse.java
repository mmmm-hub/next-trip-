package com.nexttrip.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsResponse {

	private long totalUsers;
	private long totalDestinations;
	private long totalReviews;
	private long totalWishlists;
	private long totalSearchLogs;
}
