package com.nexttrip.backend.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateRequest {

	@NotBlank
	private String destinationId;

	@NotNull
	private LocalDate checkIn;

	@NotNull
	private LocalDate checkOut;

	@Min(1)
	private int travelers;
}
