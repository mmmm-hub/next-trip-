package com.nexttrip.backend.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookings")
public class Booking {

	@Id
	private String id;

	@Indexed
	private String userEmail;

	private String destinationId;

	private LocalDate checkIn;
	private LocalDate checkOut;

	private int travelers;

	private BigDecimal totalPrice;
	private String currency;

	@Builder.Default
	private BookingStatus status = BookingStatus.CONFIRMED;

	private String invoiceNumber;

	private Instant createdAt;
}
