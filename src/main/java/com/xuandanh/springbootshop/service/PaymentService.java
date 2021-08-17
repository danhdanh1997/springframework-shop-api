package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.Payment;
import com.xuandanh.springbootshop.dto.PaymentDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.mapper.PaymentMapper;
import com.xuandanh.springbootshop.repository.CustomerRepository;
import com.xuandanh.springbootshop.repository.PaymentRepository;
import com.xuandanh.springbootshop.repository.RentalRepository;
import com.xuandanh.springbootshop.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;
    private final RentalRepository rentalRepository;

    public Optional<PaymentDTO>findOne(String paymentId){
        return Optional.of(paymentMapper
                .paymentToPaymentDTO(paymentRepository
                .findById(paymentId).orElseThrow(()->new ResourceNotFoundException("payment with id:"+paymentId+" not exist"))));
    }

    public Optional<List<PaymentDTO>>findAll(){
        return Optional.ofNullable(Optional.ofNullable(paymentMapper
                .paymentToPaymentDTO(paymentRepository
                        .findAll())).orElseThrow(() -> new ResourceNotFoundException("List payment not exist")));
    }

    public Optional<PaymentDTO>create(String staffId, String customerId, String rentalId, Payment payment){
        return Optional.ofNullable(staffRepository
                .findById(staffId)
                .map(staff -> {
                    payment.setStaff(staff);
                    customerRepository.findById(customerId)
                            .map(customer -> {
                                payment.setCustomer(customer);
                                rentalRepository.findById(rentalId)
                                        .map(rental -> {
                                            payment.setRental(rental);
                                            return paymentMapper.paymentToPaymentDTO(payment);
                                        }).orElseThrow(()->new ResourceNotFoundException("rental with id:"+rentalId+" not exist"));
                                return paymentMapper.paymentToPaymentDTO(payment);
                            }).orElseThrow(()->new ResourceNotFoundException("customer with id:"+customerId+" not exist"));
                    return paymentMapper.paymentToPaymentDTO(payment);
                }).orElseThrow(()->new ResourceNotFoundException("staff with id:"+staffId+" not exist")));
    }

    public Optional<PaymentDTO>updatePayment(Payment payment){
        return Optional.ofNullable(Optional.of(paymentRepository
                .findById(payment.getPaymentId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(payment1 -> {
                    payment1.setPaymentDate(payment.getPaymentDate());
                    payment1.setRental(payment.getRental());
                    payment1.setAmount(payment.getAmount());
                    payment1.setCustomer(payment.getCustomer());
                    payment1.setStaff(payment.getStaff());
                    return paymentMapper.paymentToPaymentDTO(paymentRepository.save(payment1));
                }).orElseThrow(() -> new ResourceNotFoundException("payment with id:" + payment.getPaymentId() + " not exist")));
    }
}
