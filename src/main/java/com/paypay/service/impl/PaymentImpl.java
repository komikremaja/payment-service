package com.paypay.service.impl;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypay.Exception.BadRequestException;
import com.paypay.constant.VariableConstant;
import com.paypay.dto.Request.InquiryPaymentRequest;
import com.paypay.dto.Request.UpdateStatusPaymentRequest;
import com.paypay.dto.Response.Response;
import com.paypay.dto.Response.ResponseInquiryPayment;
import com.paypay.dto.Response.ResponseInquiryTransaction;
import com.paypay.dto.Response.ResponsePaymentStatus;
import com.paypay.model.PaymentData;
import com.paypay.repository.PaymentRepository;

@Service
public class PaymentImpl {
    Response response;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VariableConstant variableConstant;

    @Autowired
    private ObjectMapper objectMapper;

    public ResponseInquiryPayment inquiryPayment(String vaNumber) throws Exception {
        // Get data payment if exist
        PaymentData paymentDb = paymentRepository.findByVaNumber(vaNumber);
        ResponseInquiryTransaction transactionData = inquiryTransaction(vaNumber);
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime dateTransaction = transactionData.getCreatedDate();
        Duration duration = Duration.between(dateTransaction, today);
        if (paymentDb != null) {
            try {
                if (!transactionData.getTransactionStatus().equalsIgnoreCase("1")
                        || !paymentDb.getPaymentStatus().equalsIgnoreCase("1")) {
                    throw new BadRequestException(variableConstant.getTRANSACTION_CANNOT_PAYMENT());
                }
                if (duration.toMinutes() > 10) {

                    throw new BadRequestException(variableConstant.getTRANSACTION_CANNOT_PAYMENT());
                }
            } catch (Exception e) {
                // TODO: handle exception
                // 1 = waiting payment
                // 2 = failed
                // 3 =success
                if (paymentDb.getPaymentStatus().equalsIgnoreCase("1")) {
                    paymentDb.setPaymentStatus("2");
                    paymentRepository.save(paymentDb);
                }
                throw new BadRequestException(variableConstant.getTRANSACTION_CANNOT_PAYMENT());
            }

            ResponseInquiryPayment responseInquiryPayment = mapper.map(paymentDb, ResponseInquiryPayment.class);
            return responseInquiryPayment;

        } else {
            ResponseInquiryPayment responseInquiryPayment = new ResponseInquiryPayment();
            String[] currencys = transactionData.getCurrencyPair().split("\\/");
            responseInquiryPayment.setIdTransaction(transactionData.getIdTransaction());
            responseInquiryPayment.setDestinationAccount(transactionData.getDestinationAccount());
            responseInquiryPayment.setLastUpdate(today);
            responseInquiryPayment.setCreatedDate(today);
            responseInquiryPayment.setNic(transactionData.getNic());
            responseInquiryPayment.setVaNumber(transactionData.getVaNumber());
            responseInquiryPayment.setPaymentStatus("1");
            if (transactionData.getTypeTransaction().equals("B")) {
                responseInquiryPayment.setTotalAmount(transactionData.getAmount2());
                responseInquiryPayment.setCurrency(currencys[1]);
            } else {
                responseInquiryPayment.setTotalAmount(transactionData.getAmount1());
                responseInquiryPayment.setCurrency(currencys[0]);
            }
            PaymentData paymentData = mapper.map(responseInquiryPayment, PaymentData.class);
            try {
                if (!transactionData.getTransactionStatus().equalsIgnoreCase("1")) {
                    throw new BadRequestException(variableConstant.getTRANSACTION_CANNOT_PAYMENT());
                }

                if (duration.toMinutes() > 10) {

                    throw new BadRequestException("Transaksi sudah melebihi 10 menit");
                }
            } catch (Exception e) {
                // TODO: handle exception
                paymentData.setPaymentStatus("2");
                paymentRepository.save(paymentData);
                System.out.println(e.getMessage());
                throw new BadRequestException(variableConstant.getTRANSACTION_CANNOT_PAYMENT());
            }

            paymentRepository.save(paymentData);
            responseInquiryPayment.setIdPayment(paymentData.getIdPayment());
            return responseInquiryPayment;
        }

    }

    public ResponseInquiryTransaction inquiryTransaction(String vaNumber) throws Exception {
        String url = "http://localhost:8383/transaction-service/inquiry/transaction/" + vaNumber;
        ResponseInquiryTransaction inquiryData = null;
        try {
            inquiryData = restTemplate.getForEntity(url, ResponseInquiryTransaction.class, 1).getBody();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            throw new BadRequestException("Error Inquiry: " + e.getMessage());
        }

        return inquiryData;
    }

    public Response updateStatusPayment(UpdateStatusPaymentRequest request) throws Exception {
        // Get data payment if exist
        LocalDateTime today = LocalDateTime.now();
        PaymentData paymentDb = paymentRepository.findByIdPayment(request.getIdPayment());
        if(paymentDb == null){
            throw new BadRequestException("Data Payment tidak di temukan");
        }
        paymentDb.setPaymentStatus(request.getPaymentStatus());
        paymentDb.setLastUpdate(today);
        paymentRepository.save(paymentDb);
        return response = new Response(variableConstant.getSTATUS_OK(), "Success, Update Payment Status", paymentDb);

    }

    public Response inquiryPaymentStatus(InquiryPaymentRequest request) throws Exception {
        // Get data payment if exist
        PaymentData paymentDataDB = paymentRepository.findByVaNumber(request.getVaNumber());
        if(paymentDataDB == null){
            throw new BadRequestException("Data payment tidak ditemukan");
        }
        ResponsePaymentStatus responsePaymentStatus = new ResponsePaymentStatus();
        responsePaymentStatus.setPaymentStatus(paymentDataDB.getPaymentStatus());
        responsePaymentStatus.setVaNumber(paymentDataDB.getVaNumber());

        return response = new Response(variableConstant.getSTATUS_OK(), "Success Recon", responsePaymentStatus);

    }

}
