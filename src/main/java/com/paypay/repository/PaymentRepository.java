package com.paypay.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paypay.model.PaymentData;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentData, BigDecimal> {
    
    PaymentData findByVaNumber(String vaNumber);
    PaymentData findByIdPayment(BigDecimal idPayment);
}
