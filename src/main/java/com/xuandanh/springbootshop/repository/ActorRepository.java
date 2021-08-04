package com.xuandanh.springbootshop.repository;

import com.xuandanh.springbootshop.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor,String> {
    String ACTOR_BY_ID_CACHE = "actorbyid";
}
