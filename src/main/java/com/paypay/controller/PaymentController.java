package com.paypay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paypay.dto.Request.InquiryPaymentRequest;
import com.paypay.dto.Request.UpdateStatusPaymentRequest;
import com.paypay.dto.Response.Response;
import com.paypay.dto.Response.ResponseInquiryPayment;
import com.paypay.dto.Response.ResponsePaymentStatus;
import com.paypay.service.impl.PaymentImpl;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    Response response;

    @Autowired
    private PaymentImpl paymentImpl;

    @GetMapping("/inquiry-tagihan/{vaNumber}")
    public ResponseInquiryPayment transactionExchange(@PathVariable(name = "vaNumber") String request) throws Exception {
        ResponseInquiryPayment response = paymentImpl.inquiryPayment(request);
        return response;
    }

    @PutMapping("/recon/payment-status")
    public Response reconPaymentStatus(@RequestBody UpdateStatusPaymentRequest request) throws Exception {
        response = paymentImpl.updateStatusPayment(request);
        return response;
    }

    @PostMapping("/inquiry/payment-status")
    public Response inquiryPaymentStatus(@RequestBody InquiryPaymentRequest request) throws Exception {
         response = paymentImpl.inquiryPaymentStatus(request);
        return response;
    }
}
