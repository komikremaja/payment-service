package com.paypay.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paypay.dto.Request.TransactionExchangeRequest;
import com.paypay.dto.Response.Response;
import com.paypay.service.impl.TransactionImpl;

@RestController
@RequestMapping("/transaction-service")
public class TransactionController {
    Response response;

    @Autowired
    private TransactionImpl transactionImpl;
    
    @PostMapping("/transaction")
    public Response transactionExchange(@Valid @RequestBody TransactionExchangeRequest request) throws Exception {
        response = transactionImpl.TransactionExchange(request);
        return response;
    }
    
}
