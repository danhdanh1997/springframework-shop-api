package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.Staff;
import com.xuandanh.springbootshop.dto.AddressDTO;
import com.xuandanh.springbootshop.dto.StaffDTO;
import com.xuandanh.springbootshop.dto.StoreDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.repository.StaffRepository;
import com.xuandanh.springbootshop.service.AddressService;
import com.xuandanh.springbootshop.service.StaffService;
import com.xuandanh.springbootshop.service.StoreService;
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
public class StaffResources {
    private final StaffService staffService;
    private final StaffRepository staffRepository;
    private final AddressService addressService;
    private final StoreService  storeService;
    private final Logger log = LoggerFactory.getLogger(StaffResources.class);
    public StaffService getStaffService() {
        return staffService;
    }

    public StaffRepository getStaffRepository() {
        return staffRepository;
    }

    public AddressService getAddressService() {
        return addressService;
    }

    public StoreService getStoreService() {
        return storeService;
    }

    public StaffResources(StaffService staffService, StaffRepository staffRepository, AddressService addressService, StoreService storeService) {
        this.staffService = staffService;
        this.staffRepository = staffRepository;
        this.addressService = addressService;
        this.storeService = storeService;
    }

    @GetMapping("/staff/findOne/{staffId}")
    public ResponseEntity<?>getOne(@PathVariable("staffId")String staffId){
        Optional<StaffDTO>staffDTO = staffService.findOne(staffId);
        Map<String,Boolean>response = new HashMap<>();
        if (staffDTO.isEmpty()){
            log.error("staff with id:"+staffId+" not exist");
            response.put("staff with id:"+staffId+" not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("Information of a staff with id:"+staffId+":"+staffDTO);
        return ResponseEntity.ok(staffDTO);
    }

    @GetMapping("/staff/findAll")
    public ResponseEntity<?>getAll(){
        Optional<List<StaffDTO>>staffDTOS = staffService.findAll();
        Map<String,Boolean>response = new HashMap<>();
        int size = 0;
        size = staffDTOS.map(List::size).orElse(0);
        if (size==0){
            log.error("List staff is empty");
            response.put("List staff is empty",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("Show list staff");
        return ResponseEntity.ok(staffDTOS);
    }

    @PostMapping("/address/{addressId}/store/{storeId}/staff")
    public ResponseEntity<?>create(@PathVariable("addressId")int addressId,
                                   @PathVariable("storeId")int storeId,
                                   @Valid @RequestBody Staff staff){
        Optional<AddressDTO>addressDTO = addressService.findOne(addressId);
        Optional<StoreDTO>storeDTO = storeService.findOne(storeId);
        Map<String,Boolean>response = new HashMap<>();
        if (addressDTO.isEmpty() && storeDTO.isEmpty()){
            log.error("address with id:"+addressId+" not exist"+" && "+"store with id:"+storeId+" not exist");
            response.put("address with id:"+addressId+" not exist"+" && "+"store with id:"+storeId+" not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("created staff successfully");
        return ResponseEntity.ok(staffService.createStaff(addressId,storeId,staff));
    }

    @PutMapping("/address/{addressId}/store/{storeId}/staff/{staffId}")
    public ResponseEntity<?>update(@PathVariable("addressId")int addressId,
                                   @PathVariable("storeId")int storeId,
                                   @PathVariable("staffId")String staffId,
                                   @Valid @RequestBody Staff staff){
        Optional<AddressDTO>addressDTO = addressService.findOne(addressId);
        Optional<StoreDTO>storeDTO = storeService.findOne(storeId);
        Optional<StaffDTO>staffDTO = staffService.findOne(staffId);
        Map<String,Boolean>response = new HashMap<>();
        if (addressDTO.isEmpty() && storeDTO.isEmpty() && staffDTO.isEmpty()){
            log.error("address with id:"+addressId+" not exist"+" && "+"store with id:"+storeId+" not exist"+" && "+ "staff with id:"+staffId);
            response.put("address with id:"+addressId+" not exist"+" && "+"store with id:"+storeId+" not exist"+" && "+"staff with id:"+staffId,Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("update info of a staff successfully");
        return ResponseEntity.ok(staffService.updateStaff(staff));
    }

    @DeleteMapping("/address/{addressId}/store/{storeId}/staff/{staffId}")
    public ResponseEntity<?>delete(@PathVariable("addressId")int addressId,
                                   @PathVariable("storeId")int storeId,
                                   @PathVariable("staffId")String staffId){
        Optional<AddressDTO>addressDTO = addressService.findOne(addressId);
        Optional<StoreDTO>storeDTO = storeService.findOne(storeId);
        Map<String,Boolean>response = new HashMap<>();
        return staffRepository.findById(staffId)
                .map(staff -> {
                    if (addressDTO.isEmpty() && storeDTO.isEmpty()){
                        log.error("address with id:"+addressId+" not exist"+" && "+"store with id:"+storeId+" not exist");
                        response.put("address with id:"+addressId+" not exist"+" && "+"store with id:"+storeId+" not exist",Boolean.FALSE);
                        return ResponseEntity.ok(response);
                    }
                    log.info("deleted staff successfully");
                    response.put("deleted staff successfully",Boolean.TRUE);
                    staffRepository.delete(staff);
                    return ResponseEntity.ok(response);
                }).orElseThrow(()->new ResourceNotFoundException("staff with id:"+staffId +" not exist"));
    }
}
