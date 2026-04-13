package com.nexttrip.backend.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceLineDto {

	private String description;
	private int quantity;
	private BigDecimal unitPrice;
	private BigDecimal lineTotal;
}
