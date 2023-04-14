package com.paypay.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "payment_data")
public class PaymentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment")
    private BigDecimal idPayment;

    @Column(name = "id_transaction")
    private BigDecimal idTransaction;
    
    @Column(name = "va_number")
    private String vaNumber;
    
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    
    @Column(name = "source_account")
    private String sourceAccount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "destination_account")
    private String destinationAccount;
    
    @Column(name = "bank_payment")
    private String bankPayment;

    @Column(name = "nic")
    private String nic;

    @Column(name = "payment_status")
    private String paymentStatus;
    
    @Column(name = "created_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @Column(name = "last_update")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdate;
}
