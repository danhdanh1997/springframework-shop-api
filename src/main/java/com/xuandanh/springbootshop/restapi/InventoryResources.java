package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.Inventory;
import com.xuandanh.springbootshop.dto.FilmDTO;
import com.xuandanh.springbootshop.dto.InventoryDTO;
import com.xuandanh.springbootshop.dto.StoreDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.repository.InventoryRepository;
import com.xuandanh.springbootshop.service.FilmService;
import com.xuandanh.springbootshop.service.InventoryService;
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
public class InventoryResources {
    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;
    private final FilmService filmService;
    private final StoreService storeService;
    private final Logger log = LoggerFactory.getLogger(InventoryResources.class);

    public FilmService getFilmService() {
        return filmService;
    }

    public StoreService getStoreService() {
        return storeService;
    }

    public InventoryRepository getInventoryRepository() {
        return inventoryRepository;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public InventoryResources(StoreService storeService,FilmService filmService,InventoryRepository inventoryRepository, InventoryService inventoryService) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryService = inventoryService;
        this.filmService = filmService;
        this.storeService = storeService;
    }

    @GetMapping("/inventory/findOne/{inventoriesId}")
    public ResponseEntity<?>getOne(@PathVariable("inventoriesId")int inventoriesId){
        Optional<InventoryDTO>inventoryDTO = inventoryService.findOne(inventoriesId);
        Map<String,Boolean>response = new HashMap<>();
        if (inventoryDTO.isEmpty()){
            log.error("inventory with id:"+inventoriesId+" not exits");
            response.put("inventory with id:"+inventoriesId+" not exits",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("Information of a inventory with id:"+inventoriesId+":"+inventoryDTO);
        return ResponseEntity.ok(inventoryDTO);
    }

    @GetMapping("/inventory/findAll")
    public ResponseEntity<?>getAll(){
        Optional<List<InventoryDTO>>inventoryDTOList = inventoryService.findAll();
        Map<String,Boolean>response = new HashMap<>();
        if (inventoryDTOList.stream().count()==0){
            log.error("List inventory is empty");
            response.put("List inventory is empty",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("Show list inventory :"+inventoryDTOList);
        return ResponseEntity.ok(inventoryDTOList);
    }

    @PostMapping("/film/{filmId}/store/{storeId}/inventory")
    public ResponseEntity<?>createInventory(@PathVariable("filmId")String filmId,
                                            @PathVariable("storeId")int storeId,
                                            @Valid @RequestBody Inventory inventory){
        Optional<FilmDTO>filmDTO = filmService.findOne(filmId);
        Optional<StoreDTO>storeDTO = storeService.findOne(storeId);
        Map<String,Boolean>response = new HashMap<>();
        if (filmDTO.isEmpty() && storeDTO.isEmpty()){
            log.error("film with id :"+filmId+" not exist"+" && "+ "store with id:"+storeId+" not exist");
            response.put("film with id :"+filmId+" not exist"+" && "+ "store with id:"+storeId+" not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("created inventory was successfully");
        return ResponseEntity.ok(inventoryService.createInventory(filmId,storeId,inventory));
    }

    @PutMapping("/film/{filmId}/store/{storeId}/inventory/{inventoriesId}")
    public ResponseEntity<?>update(@PathVariable("filmId")String filmId,
                                   @PathVariable("storeId")int storeId,
                                   @PathVariable("inventoriesId")int inventoriesId,
                                   @Valid @RequestBody Inventory inventory){
        Optional<FilmDTO>filmDTO = filmService.findOne(filmId);
        Optional<StoreDTO>storeDTO = storeService.findOne(storeId);
        Optional<InventoryDTO>inventoryDTO = inventoryService.findOne(inventoriesId);
        Map<String,Boolean>response = new HashMap<>();
        if (filmDTO.isEmpty() && storeDTO.isEmpty() && inventoryDTO.isEmpty()){
            log.error("film with id :"+filmId+" not exist"+" && "+ "store with id:"+storeId+" not exist"+"&& "+"inventory not found");
            response.put("film with id :"+filmId+" not exist"+" && "+ "store with id:"+storeId+" not exist "+"&& "+"inventory not found",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(inventoryService.updateInventory(inventory));
    }

    @DeleteMapping("/film/{filmId}/store/{storeId}/inventory/{inventoriesId}")
    public ResponseEntity<?>delete(@PathVariable("filmId")String filmId,
                                   @PathVariable("storeId")int storeId,
                                   @PathVariable("inventoriesId")int inventoriesId){
        Optional<FilmDTO>filmDTO = filmService.findOne(filmId);
        Optional<StoreDTO>storeDTO = storeService.findOne(storeId);
        Optional<InventoryDTO>inventoryDTO = inventoryService.findOne(inventoriesId);
        Map<String,Boolean>response = new HashMap<>();
        return inventoryRepository.findById(inventoriesId)
                .map(inventory -> {
                    if (filmDTO.isEmpty() && storeDTO.isEmpty() && inventoryDTO.isEmpty()){
                        log.error("film with id :"+filmId+" not exist"+" && "+ "store with id:"+storeId+" not exist"+"&& "+"inventory not found");
                        response.put("film with id :"+filmId+" not exist"+" && "+ "store with id:"+storeId+" not exist "+"&& "+"inventory not found",Boolean.FALSE);
                        return ResponseEntity.ok(response);
                    }
                    log.info("deleted inventory successfully");
                    response.put("deleted inventory successfully",Boolean.TRUE);
                    inventoryRepository.delete(inventory);
                    return ResponseEntity.ok(response);
                }).orElseThrow(()->new ResourceNotFoundException("delete inventory is faild"));
    }
}
