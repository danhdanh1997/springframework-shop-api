package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.Inventory;
import com.xuandanh.springbootshop.dto.InventoryDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.mapper.InventoryMapper;
import com.xuandanh.springbootshop.repository.FilmRepository;
import com.xuandanh.springbootshop.repository.InventoryRepository;
import com.xuandanh.springbootshop.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final FilmRepository filmRepository;
    private final StoreRepository storeRepository;
    public InventoryRepository getInventoryRepository() {
        return inventoryRepository;
    }

    public FilmRepository getFilmRepository() {
        return filmRepository;
    }

    public StoreRepository getStoreRepository() {
        return storeRepository;
    }

    public InventoryMapper getInventoryMapper() {
        return inventoryMapper;
    }

    public InventoryService( StoreRepository storeRepository,FilmRepository filmRepository,InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
        this.filmRepository = filmRepository;
        this.storeRepository = storeRepository;
    }

    public Optional<InventoryDTO>findOne(int inventoriesId){
        return Optional.ofNullable(inventoryMapper
                .inventoryToInventoryDTO(inventoryRepository
                .findById(inventoriesId)
                .orElseThrow(()->new ResourceNotFoundException("inventory with id:"+inventoriesId+" not exist"))));
    }

    public Optional<List<InventoryDTO>>findAll(){
        return Optional.ofNullable(Optional.ofNullable(inventoryMapper
                .inventoryToInventoryDTO(inventoryRepository
                .findAll()))
                .orElseThrow(() -> new ResourceNotFoundException("List inventory is empty")));
    }

    public Optional<InventoryDTO>createInventory(String filmId , int storeId, Inventory inventory){
        return Optional.ofNullable(filmRepository
                .findById(filmId)
                .stream().map(film -> {
                    inventory.setFilm(film);
                    storeRepository.findById(storeId)
                            .stream().map(store -> {
                                inventory.setStore(store);
                                return inventoryMapper.inventoryToInventoryDTO(inventoryRepository.save(inventory));
                    }).findAny().orElseThrow(()->new ResourceNotFoundException("store with id:"+storeId+" not exist"));
                    return inventoryMapper.inventoryToInventoryDTO(inventoryRepository.save(inventory));
                }).findAny().orElseThrow(()->new ResourceNotFoundException("film with id:"+filmId+" not exist")));
    }

    public Optional<InventoryDTO>updateInventory(Inventory inventory){
        return Optional.ofNullable(Optional.of(inventoryRepository
                .findById(inventory.getInventoriesId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(inventory1 -> {
                    inventory1.setStore(inventory.getStore());
                    inventory1.setFilm(inventory.getFilm());
                    inventory1.setLastUpdate(inventory.getLastUpdate());
                    return inventoryMapper.inventoryToInventoryDTO(inventoryRepository.save(inventory1));
                }).orElseThrow(() -> new ResourceNotFoundException("inventory with id:" + inventory.getInventoriesId() + " not exist")));
    }
}
