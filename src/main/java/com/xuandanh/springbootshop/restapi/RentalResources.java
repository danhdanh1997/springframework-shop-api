package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.Rental;
import com.xuandanh.springbootshop.dto.CustomerDTO;
import com.xuandanh.springbootshop.dto.InventoryDTO;
import com.xuandanh.springbootshop.dto.RentalDTO;
import com.xuandanh.springbootshop.dto.StaffDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.repository.RentalRepository;
import com.xuandanh.springbootshop.service.CustomerService;
import com.xuandanh.springbootshop.service.InventoryService;
import com.xuandanh.springbootshop.service.RentalService;
import com.xuandanh.springbootshop.service.StaffService;
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
public class RentalResources {
    private final RentalService rentalService;
    private final RentalRepository rentalRepository;
    private final InventoryService inventoryService;
    private final CustomerService customerService;
    private final StaffService staffService;
    private final Logger log = LoggerFactory.getLogger(RentalResources.class);

    public RentalService getRentalService() {
        return rentalService;
    }

    public RentalRepository getRentalRepository() {
        return rentalRepository;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public StaffService getStaffService() {
        return staffService;
    }

    public RentalResources(RentalService rentalService, RentalRepository rentalRepository, InventoryService inventoryService, CustomerService customerService, StaffService staffService) {
        this.rentalService = rentalService;
        this.rentalRepository = rentalRepository;
        this.inventoryService = inventoryService;
        this.customerService = customerService;
        this.staffService = staffService;
    }

    @GetMapping("/rental/findOne/{rentalId}")
    public ResponseEntity<?>getOne(@PathVariable("rentalId")String rentalId){
        Optional<RentalDTO>rentalDTO = rentalService.findOne(rentalId);
        Map<String,Boolean>response = new HashMap<>();
        if (rentalDTO.isEmpty()){
            log.error("rental with id:"+rentalId+" not exist");
            response.put("rental with id:"+rentalId+" not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("information of a rental with id:"+rentalId+":"+rentalDTO);
        return ResponseEntity.ok(rentalDTO);
    }

    @GetMapping("/rental/findAll")
    public ResponseEntity<?>getAll(){
        Optional<List<RentalDTO>>rentalDTOList = rentalService.findAll();
        Map<String,Boolean>response = new HashMap<>();
        int size = 0;
        size = rentalDTOList.map(List::size).orElse(0);
        if (size==0){
            log.error("List rental is empty");
            response.put("List rental is empty",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("Show list of rental:"+rentalDTOList);
        return ResponseEntity.ok(rentalDTOList);
    }

    @PostMapping("/inventory/{inventoriesId}/customer/{customerId}/staff/{staffId}/rental")
    public ResponseEntity<?>create(@PathVariable("inventoriesId")int inventoriesId,
                                   @PathVariable("customerId")String customerId,
                                   @PathVariable("staffId")String staffId,
                                   @Valid @RequestBody Rental rental){
        Optional<InventoryDTO>inventoryDTO = inventoryService.findOne(inventoriesId);
        Optional<CustomerDTO>customerDTO = customerService.findOne(customerId);
        Optional<StaffDTO>staffDTO = staffService.findOne(staffId);
        Map<String,Boolean>response = new HashMap<>();
        if (inventoryDTO.isEmpty() && customerDTO.isEmpty() && staffDTO.isEmpty()){
            log.error("inventory with id:"+inventoriesId+" not exist"+" && "+"customer with id:"+customerId+" not exist"+" && "+"staff with id:"+staffId+" not exist" );
            response.put("inventory with id:"+inventoriesId+" not exist"+" && "+"customer with id:"+customerId+" not exist"+" && "+"staff with id:"+staffId+" not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("created rental successfully");
        return ResponseEntity.ok(rentalService.createRental(inventoriesId,customerId,staffId,rental));
    }

    @PutMapping("/inventory/{inventoriesId}/customer/{customerId}/staff/{staffId}/rental/{rentalId}")
    public ResponseEntity<?>update(@PathVariable("inventoriesId")int inventoriesId,
                                   @PathVariable("customerId")String customerId,
                                   @PathVariable("staffId")String staffId,
                                   @PathVariable("rentalId")String rentalId,
                                   @Valid @RequestBody Rental rental){
        Optional<InventoryDTO>inventoryDTO = inventoryService.findOne(inventoriesId);
        Optional<CustomerDTO>customerDTO = customerService.findOne(customerId);
        Optional<StaffDTO>staffDTO = staffService.findOne(staffId);
        Optional<RentalDTO>rentalDTO = rentalService.findOne(rentalId);
        Map<String,Boolean>response = new HashMap<>();
        if (inventoryDTO.isEmpty() && customerDTO.isEmpty() && staffDTO.isEmpty() && rentalDTO.isEmpty()){
            log.error("inventory with id:"+inventoriesId+" not exist"+" && "+"customer with id:"+customerId+" not exist"+" && "+"staff with id:"+staffId+" not exist" +" && "+" rental with id:"+rentalId+" not exist" );
            response.put("inventory with id:"+inventoriesId+" not exist"+" && "+"customer with id:"+customerId+" not exist"+" && "+"staff with id:"+staffId+" not exist"+" && "+" rental with id:"+rentalId+" not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("update rental successfully");
        return ResponseEntity.ok(rentalService.updateRental(rental));
    }

    @DeleteMapping("/inventory/{inventoriesId}/customer/{customerId}/staff/{staffId}/rental/{rentalId}")
    public ResponseEntity<?>delete(@PathVariable("inventoriesId")int inventoriesId,
                                   @PathVariable("customerId")String customerId,
                                   @PathVariable("staffId")String staffId,
                                   @PathVariable("rentalId")String rentalId){
        Optional<InventoryDTO>inventoryDTO = inventoryService.findOne(inventoriesId);
        Optional<CustomerDTO>customerDTO = customerService.findOne(customerId);
        Optional<StaffDTO>staffDTO = staffService.findOne(staffId);
        Map<String,Boolean>response = new HashMap<>();
        return rentalRepository.findById(rentalId)
                .map(rental -> {
                    if (inventoryDTO.isEmpty() && customerDTO.isEmpty() && staffDTO.isEmpty()){
                        log.error("inventory with id:"+inventoriesId+" not exist"+" && "+"customer with id:"+customerId+" not exist"+" && "+"staff with id:"+staffId+" not exist" );
                        response.put("inventory with id:"+inventoriesId+" not exist"+" && "+"customer with id:"+customerId+" not exist"+" && "+"staff with id:"+staffId+" not exist",Boolean.FALSE);
                        return ResponseEntity.ok(response);
                    }
                    log.info("deleted rental with id:"+rentalId+ "successfully");
                    rentalRepository.delete(rental);
                    response.put("deleted rental with id:"+rentalId+ "successfully",Boolean.TRUE);
                    return ResponseEntity.ok(response);
                }).orElseThrow(()->new ResourceNotFoundException("rental with id:"+rentalId+" not exist"));
    }
}
