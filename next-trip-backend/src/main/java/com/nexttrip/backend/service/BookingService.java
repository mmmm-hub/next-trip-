package com.nexttrip.backend.service;

import java.util.List;

import com.nexttrip.backend.dto.request.BookingCreateRequest;
import com.nexttrip.backend.dto.response.BookingResponse;
import com.nexttrip.backend.dto.response.InvoiceResponse;

public interface BookingService {

	BookingResponse createBooking(String userEmail, BookingCreateRequest request);

	List<BookingResponse> listMyBookings(String userEmail);

	InvoiceResponse getInvoice(String userEmail, String bookingId);
}
