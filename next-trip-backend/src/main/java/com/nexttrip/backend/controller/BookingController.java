package com.nexttrip.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexttrip.backend.dto.request.BookingCreateRequest;
import com.nexttrip.backend.dto.response.BookingResponse;
import com.nexttrip.backend.dto.response.InvoiceResponse;
import com.nexttrip.backend.security.SecurityUtils;
import com.nexttrip.backend.service.BookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

	private final BookingService bookingService;

	@PostMapping
	public ResponseEntity<BookingResponse> create(@Valid @RequestBody BookingCreateRequest request) {
		String email = SecurityUtils.currentUserEmail();
		if (email == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(email, request));
	}

	@GetMapping("/me")
	public ResponseEntity<List<BookingResponse>> mine() {
		String email = SecurityUtils.currentUserEmail();
		if (email == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.ok(bookingService.listMyBookings(email));
	}

	@GetMapping("/{id}/invoice")
	public ResponseEntity<InvoiceResponse> invoice(@PathVariable String id) {
		String email = SecurityUtils.currentUserEmail();
		if (email == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.ok(bookingService.getInvoice(email, id));
	}
}
