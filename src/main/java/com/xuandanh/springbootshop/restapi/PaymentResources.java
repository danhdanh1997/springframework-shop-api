package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.Payment;
import com.xuandanh.springbootshop.dto.CustomerDTO;
import com.xuandanh.springbootshop.dto.PaymentDTO;
import com.xuandanh.springbootshop.dto.RentalDTO;
import com.xuandanh.springbootshop.dto.StaffDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.repository.PaymentRepository;
import com.xuandanh.springbootshop.service.CustomerService;
import com.xuandanh.springbootshop.service.PaymentService;
import com.xuandanh.springbootshop.service.RentalService;
import com.xuandanh.springbootshop.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentResources {
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final Logger log = LoggerFactory.getLogger(PaymentResources.class);
    private final StaffService staffService;
    private final CustomerService customerService;
    private final RentalService rentalService;

    @GetMapping("/payment/findOne/{paymentId}")
    public ResponseEntity<?>getOne(@PathVariable("paymentId")String paymentId){
        Optional<PaymentDTO>paymentDTO = paymentService.findOne(paymentId);
        Map<String,Boolean>response = new HashMap<>();
        if (paymentDTO.isEmpty()){
            log.error("payment with id:"+paymentId+" not exist");
            response.put("payment with id:"+paymentId+" not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("info of payment:"+paymentDTO);
        return ResponseEntity.ok(paymentDTO);
    }

    @GetMapping("/payment/findAll")
    public ResponseEntity<?>getAll(){
        Optional<List<PaymentDTO>>paymentDTOList = paymentService.findAll();
        Map<String,Boolean>response = new HashMap<>();
        int size =0;
        size = paymentDTOList.map(List::size).orElse(0);
        if (size ==0){
            log.error("List payment is empty");
            response.put("List payment is empty",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("Show list payment:"+paymentDTOList);
        return ResponseEntity.ok(paymentDTOList);
    }

    @PostMapping("/staff/{staffId}/customer/{customerId}/rental/{rentalId}/payment")
    public ResponseEntity<?>create(@PathVariable("staffId")String staffId,
                                   @PathVariable("customerId")String customerId,
                                   @PathVariable("rentalId")String rentalId,
                                   @Valid @RequestBody Payment payment){
        Optional<StaffDTO>staffDTO = staffService.findOne(staffId);
        Optional<CustomerDTO>customerDTO = customerService.findOne(customerId);
        Optional<RentalDTO>rentalDTO = rentalService.findOne(rentalId);
        Map<String,Boolean>response = new HashMap<>();

        if (staffDTO.isEmpty() && customerDTO.isEmpty() && rentalDTO.isEmpty()){
            log.error("staff && customer && rental not found");
            response.put("staff && customer && rental not found",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("created payment successfully");
        return ResponseEntity.ok(paymentService.create(staffId,customerId,rentalId,payment));
    }

    @PutMapping("/staff/{staffId}/customer/{customerId}/rental/{rentalId}/payment/{paymentId}")
    public ResponseEntity<?>update(@PathVariable("staffId")String staffId,
                                   @PathVariable("customerId")String customerId,
                                   @PathVariable("rentalId")String rentalId,
                                   @PathVariable("paymentId")String paymentId,
                                   @Valid @RequestBody Payment payment){
        Optional<StaffDTO>staffDTO = staffService.findOne(staffId);
        Optional<CustomerDTO>customerDTO = customerService.findOne(customerId);
        Optional<RentalDTO>rentalDTO = rentalService.findOne(rentalId);
        Optional<PaymentDTO>paymentDTO = paymentService.findOne(paymentId);
        Map<String,Boolean>response = new HashMap<>();

        if (staffDTO.isEmpty() && customerDTO.isEmpty() && rentalDTO.isEmpty() && paymentDTO.isEmpty()){
            log.error("staff && customer && rental && payment not found");
            response.put("staff && customer && rental && payment not found",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("update info of payment successfully");
        return ResponseEntity.ok(paymentService.updatePayment(payment));
    }

    @DeleteMapping("/staff/{staffId}/customer/{customerId}/rental/{rentalId}/payment/{paymentId}")
    public ResponseEntity<?>delete(@PathVariable("staffId")String staffId,
                                   @PathVariable("customerId")String customerId,
                                   @PathVariable("rentalId")String rentalId,
                                   @PathVariable("paymentId")String paymentId){
        Optional<StaffDTO>staffDTO = staffService.findOne(staffId);
        Optional<CustomerDTO>customerDTO = customerService.findOne(customerId);
        Optional<RentalDTO>rentalDTO = rentalService.findOne(rentalId);
        Optional<PaymentDTO>paymentDTO = paymentService.findOne(paymentId);
        Map<String,Boolean>response = new HashMap<>();
        return paymentRepository.findById(paymentId)
                .map(payment -> {
                    if (staffDTO.isEmpty() && customerDTO.isEmpty() && rentalDTO.isEmpty() && paymentDTO.isEmpty()){
                        log.error("staff && customer && rental && payment not found");
                        response.put("staff && customer && rental && payment not found",Boolean.FALSE);
                        return ResponseEntity.ok(response);
                    }
                    log.info("deleted info of payment successfully");
                    paymentRepository.delete(payment);
                    response.put("payment with id:"+paymentId+" was deleted successfully",Boolean.TRUE);
                    return ResponseEntity.ok(response);
                }).orElseThrow(()-> new ResourceNotFoundException("payment with id:"+paymentId+" not exist"));
    }
}
