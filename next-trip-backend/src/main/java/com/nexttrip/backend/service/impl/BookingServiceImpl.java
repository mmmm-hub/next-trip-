package com.nexttrip.backend.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nexttrip.backend.dto.request.BookingCreateRequest;
import com.nexttrip.backend.dto.response.BookingResponse;
import com.nexttrip.backend.dto.response.InvoiceLineDto;
import com.nexttrip.backend.dto.response.InvoiceResponse;
import com.nexttrip.backend.exception.BadRequestException;
import com.nexttrip.backend.exception.ResourceNotFoundException;
import com.nexttrip.backend.model.Booking;
import com.nexttrip.backend.model.BookingStatus;
import com.nexttrip.backend.model.Destination;
import com.nexttrip.backend.repository.BookingRepository;
import com.nexttrip.backend.repository.DestinationRepository;
import com.nexttrip.backend.service.BookingService;
import com.nexttrip.backend.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

	private static final BigDecimal TAX_RATE = new BigDecimal("0.10");

	private final BookingRepository bookingRepository;
	private final DestinationRepository destinationRepository;
	private final NotificationService notificationService;

	@Override
	public BookingResponse createBooking(String userEmail, BookingCreateRequest request) {
		if (request.getCheckOut().isBefore(request.getCheckIn())) {
			throw new BadRequestException("checkOut must be after checkIn");
		}
		Destination dest = destinationRepository.findById(request.getDestinationId())
				.orElseThrow(() -> new ResourceNotFoundException("Destination", request.getDestinationId()));
		long nights = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
		if (nights < 1) {
			nights = 1;
		}
		BigDecimal nightly = dest.getAverageHotelPrice() != null
				? dest.getAverageHotelPrice()
				: dest.getPriceFrom() != null ? dest.getPriceFrom() : BigDecimal.valueOf(100);
		BigDecimal stay = nightly.multiply(BigDecimal.valueOf(nights)).multiply(BigDecimal.valueOf(request.getTravelers()));
		String currency = dest.getCurrency() != null ? dest.getCurrency() : "EUR";
		String invoiceNo = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
		Booking booking = Booking.builder()
				.userEmail(userEmail)
				.destinationId(dest.getId())
				.checkIn(request.getCheckIn())
				.checkOut(request.getCheckOut())
				.travelers(request.getTravelers())
				.totalPrice(stay.setScale(2, RoundingMode.HALF_UP))
				.currency(currency)
				.status(BookingStatus.CONFIRMED)
				.invoiceNumber(invoiceNo)
				.createdAt(Instant.now())
				.build();
		Booking saved = bookingRepository.save(booking);
		notificationService.notifyUser(userEmail,
				"Réservation confirmée pour " + dest.getName() + " — facture " + invoiceNo);
		return toResponse(saved);
	}

	@Override
	public List<BookingResponse> listMyBookings(String userEmail) {
		return bookingRepository.findByUserEmailOrderByCreatedAtDesc(userEmail).stream()
				.map(this::toResponse)
				.toList();
	}

	@Override
	public InvoiceResponse getInvoice(String userEmail, String bookingId) {
		Booking b = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId));
		if (!userEmail.equalsIgnoreCase(b.getUserEmail())) {
			throw new BadRequestException("Invoice not available for this user");
		}
		Destination dest = destinationRepository.findById(b.getDestinationId())
				.orElseThrow(() -> new ResourceNotFoundException("Destination", b.getDestinationId()));
		long nights = ChronoUnit.DAYS.between(b.getCheckIn(), b.getCheckOut());
		if (nights < 1) {
			nights = 1;
		}
		BigDecimal nightly = dest.getAverageHotelPrice() != null
				? dest.getAverageHotelPrice()
				: dest.getPriceFrom() != null ? dest.getPriceFrom() : BigDecimal.valueOf(100);
		BigDecimal lineStay = nightly.multiply(BigDecimal.valueOf(nights)).multiply(BigDecimal.valueOf(b.getTravelers()));
		BigDecimal subtotal = lineStay.setScale(2, RoundingMode.HALF_UP);
		BigDecimal taxes = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
		BigDecimal total = subtotal.add(taxes);
		List<InvoiceLineDto> lines = new ArrayList<>();
		lines.add(InvoiceLineDto.builder()
				.description("Hébergement — " + dest.getName() + " (" + nights + " nuits × " + b.getTravelers() + " pers.)")
				.quantity(1)
				.unitPrice(subtotal)
				.lineTotal(subtotal)
				.build());
		return InvoiceResponse.builder()
				.invoiceNumber(b.getInvoiceNumber())
				.issuedAt(b.getCreatedAt())
				.billedToEmail(userEmail)
				.destinationName(dest.getName())
				.currency(b.getCurrency())
				.lines(lines)
				.subtotal(subtotal)
				.taxes(taxes)
				.total(total)
				.build();
	}

	private BookingResponse toResponse(Booking b) {
		return BookingResponse.builder()
				.id(b.getId())
				.destinationId(b.getDestinationId())
				.checkIn(b.getCheckIn())
				.checkOut(b.getCheckOut())
				.travelers(b.getTravelers())
				.totalPrice(b.getTotalPrice())
				.currency(b.getCurrency())
				.status(b.getStatus())
				.invoiceNumber(b.getInvoiceNumber())
				.createdAt(b.getCreatedAt())
				.build();
	}
}
