package com.xuandanh.springbootshop.mapper;

import com.xuandanh.springbootshop.domain.Inventory;
import com.xuandanh.springbootshop.dto.InventoryDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class InventoryMapper {
    public List<InventoryDTO> inventoryToInventoryDTO(List<Inventory> inventoryList) {
        return inventoryList.stream().filter(Objects::nonNull).map(this::inventoryToInventoryDTO).collect(Collectors.toList());
    }

    public InventoryDTO inventoryToInventoryDTO(Inventory inventory){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(inventory,InventoryDTO.class);
    }

    public List<Inventory> inventoryDTOToInventory(List<InventoryDTO> inventoryDTOList) {
        return inventoryDTOList.stream().filter(Objects::nonNull).map(this::inventoryDTOToInventory).collect(Collectors.toList());
    }

    public Inventory inventoryDTOToInventory(InventoryDTO inventoryDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(inventoryDTO , Inventory.class);
    }
}
