package com.nexttrip.backend.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {

	private String invoiceNumber;
	private Instant issuedAt;
	private String billedToEmail;
	private String destinationName;
	private String currency;
	private List<InvoiceLineDto> lines;
	private BigDecimal subtotal;
	private BigDecimal taxes;
	private BigDecimal total;
}
