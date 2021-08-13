package com.xuandanh.springbootshop.repository;

import com.xuandanh.springbootshop.domain.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor,String> {
    String ACTOR_BY_ID_CACHE = "actorbyid";
    Slice<Actor> findAllByLastName (String lastName, Pageable pageable);
    Page<Actor> findByLastName(String lastName, Pageable pageable);
    Page<Actor> findByFirstName(String firstName, Pageable pageable);
    @Query("SELECT a FROM Actor a WHERE a.lastName like %:lastName% ORDER BY a.firstName ASC")
    Page<Actor> findByLastNameOrderByFirstName(@Param("lastName") String lastName, Pageable pageable);

}
