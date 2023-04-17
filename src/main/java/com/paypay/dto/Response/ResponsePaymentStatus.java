package com.paypay.dto.Response;

import lombok.Data;

@Data
public class ResponsePaymentStatus {
    
    private String vaNumber;
    private String paymentStatus;
}
