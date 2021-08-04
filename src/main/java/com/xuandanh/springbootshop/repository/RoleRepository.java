package com.xuandanh.springbootshop.repository;

import com.xuandanh.springbootshop.domain.ERole;
import com.xuandanh.springbootshop.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}