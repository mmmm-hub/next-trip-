package com.nexttrip.backend.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import com.nexttrip.backend.model.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

	private String id;
	private String destinationId;
	private LocalDate checkIn;
	private LocalDate checkOut;
	private int travelers;
	private BigDecimal totalPrice;
	private String currency;
	private BookingStatus status;
	private String invoiceNumber;
	private Instant createdAt;
}
