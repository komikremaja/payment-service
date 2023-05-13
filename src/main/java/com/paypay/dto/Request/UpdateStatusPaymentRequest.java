package com.paypay.dto.Request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UpdateStatusPaymentRequest {
    
    private BigDecimal idPayment;
    private String paymentStatus;
    private String sourceAccount;
    private String bankPayment;
}
