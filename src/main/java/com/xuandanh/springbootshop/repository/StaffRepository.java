package com.xuandanh.springbootshop.repository;

import com.xuandanh.springbootshop.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff,String> {
}
