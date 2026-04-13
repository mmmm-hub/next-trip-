package com.nexttrip.backend.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.nexttrip.backend.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

	private String id;
	private String email;
	private String fullName;
	private UserRole role;

	@Builder.Default
	private List<String> preferredClimateTags = new ArrayList<>();
	@Builder.Default
	private List<String> preferredAmbianceTags = new ArrayList<>();
	private BigDecimal budgetMin;
	private BigDecimal budgetMax;
}
