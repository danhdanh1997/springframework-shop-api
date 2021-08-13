package com.xuandanh.springbootshop.repository;

import com.xuandanh.springbootshop.domain.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film,String> {
    Page<Film> findByTitle(String title, Pageable pageable);
    Page<Film>findByRentalRate(String rentalRate, Pageable pageable);
    List<Film> findByRentalRate(String rentalRate, Sort sort);
}
