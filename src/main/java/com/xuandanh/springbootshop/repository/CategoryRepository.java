package com.xuandanh.springbootshop.repository;

import com.xuandanh.springbootshop.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    @Query("select c from Category c where c.categoriesName = :#{#req.categoriesName} and c.lastUpdate = :#{#req.lastUpdate}")
    Optional<Category> findByCategoryName(@Param("req") Category req);
}
