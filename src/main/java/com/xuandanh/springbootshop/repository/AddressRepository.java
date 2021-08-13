package com.xuandanh.springbootshop.repository;

import com.xuandanh.springbootshop.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Integer> {
}
