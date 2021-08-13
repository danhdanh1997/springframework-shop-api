package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.Actor;
import com.xuandanh.springbootshop.dto.ActorDTO;
import com.xuandanh.springbootshop.mapper.ActorMapper;
import com.xuandanh.springbootshop.repository.ActorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ActorService {
    private final Logger log = LoggerFactory.getLogger(ActorService.class);
    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;
    private final CacheManager cacheManager;

    public ActorRepository getActorRepository() {
        return actorRepository;
    }

    public ActorMapper getActorMapper() {
        return actorMapper;
    }

    public ActorService(ActorRepository actorRepository, ActorMapper actorMapper,CacheManager cacheManager){
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
        this.cacheManager = cacheManager;
    }

    public Optional<ActorDTO>findOne(String id){
        log.info("Show Information of an Actor: {}", id);
        return Optional.ofNullable(actorMapper.actorToactorDTO(actorRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("actor:" + id + "not exist"))));
    }

    public List<ActorDTO>findAll(){
        log.debug("show list Information for Actor: {}");
        return actorMapper.actorToactorDTOs(actorRepository.findAll());
    }

    public ActorDTO createActor(Actor actor){
        log.debug("Created Information for Actor: {}", actor);
        return actorMapper.actorToactorDTO(actorRepository.save(actor));
    }

    public Optional<ActorDTO> updateActor(Actor actor){
        return Optional.of(actorRepository
                .findById(actor.getActorId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(actor1 ->{
                    actor1.setActorId(actor.getActorId());
                    actor1.setFirstName(actor.getFirstName());
                    actor1.setLastName(actor.getLastName());
                    actor1.setLastUpdate(actor.getLastUpdate());
                    //this.clearActorCaches(actor1);
                   // log.info("Changed Information for Actor: {}", actor);
                    return actorMapper.actorToactorDTO(actorRepository.save(actor1));
                });
    }

    public void clearActorCaches(Actor actor){
        Objects.requireNonNull(cacheManager.getCache(actorRepository.ACTOR_BY_ID_CACHE)).evict(actor.getActorId());
    }

    public void deleteActor(ActorDTO actor){
        actorRepository.findById(actor.getActorId())
                .ifPresent(actor1 -> {
                    actorRepository.delete(actor1);
                    //this.clearActorCaches(actor1);
                    log.debug("Deleted Actor: {}", actor);
                });
    }

}
