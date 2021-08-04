package com.xuandanh.springbootshop.mapper;

import com.xuandanh.springbootshop.domain.Payment;
import com.xuandanh.springbootshop.dto.PaymentDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class PaymentMapper {
    public List<PaymentDTO> paymentToPaymentDTO(List<Payment> paymentList) {
        return paymentList.stream().filter(Objects::nonNull).map(this::paymentToPaymentDTO).collect(Collectors.toList());
    }

    public PaymentDTO paymentToPaymentDTO(Payment payment){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(payment,PaymentDTO.class);
    }

    public List<Payment> paymentDTOToPayment(List<PaymentDTO> paymentDTOList) {
        return paymentDTOList.stream().filter(Objects::nonNull).map(this::paymentDTOToPayment).collect(Collectors.toList());
    }

    public Payment paymentDTOToPayment(PaymentDTO paymentDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(paymentDTO , Payment.class);
    }
}
