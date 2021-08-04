package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.Actor;
import com.xuandanh.springbootshop.dto.ActorDTO;
import com.xuandanh.springbootshop.service.ActorService;
import com.xuandanh.springbootshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ActorResources {
    private final ActorService actorService;
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    public ActorService getActorService() {
        return actorService;
    }

    public ActorResources(ActorService actorService){
        this.actorService = actorService;
    }

    @GetMapping("/actor/{actorId}")
    public ResponseEntity getOne(@PathVariable("actorId")String actorId){
        Optional<ActorDTO>actor = actorService.findOne(actorId);
        if(actor.isEmpty()){
            log.error("actor:"+actorId +" not exist");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actor.get());
    }

    @GetMapping("/actors")
    public ResponseEntity<List<ActorDTO>>getAll(){
       List<ActorDTO>actorDTOList = actorService.findAll();
       if(actorDTOList.isEmpty()){
           log.error("list actor not exist");
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(actorDTOList);
    }

    @PostMapping("/actor")
    public ResponseEntity create(@Valid @RequestBody Actor actor){
        ActorDTO actorDTO = actorService.createActor(actor);
        if(actorDTO==null){
            return (ResponseEntity) ResponseEntity.noContent();
        }
        return ResponseEntity.ok(actorDTO);
    }

    @PutMapping("/actor/{actorId}")
    public ResponseEntity update(@PathVariable("actorId")String actorId,@RequestBody Actor actor){
        Optional<ActorDTO>actor1 = actorService.findOne(actorId);
        if(actor1.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actorService.updateActor(actor));
    }

    @DeleteMapping("/actor/{actorId}")
    public ResponseEntity<Map<String,Boolean>>delete(@PathVariable("actorId")String actorId){
        Optional<ActorDTO>actorDTO = actorService.findOne(actorId);
        Map<String,Boolean>response = new HashMap<>();
        if(actorDTO.isEmpty()){
            response.put("actor:"+actorId+" not found",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        actorService.deleteActor(actorDTO.get());
        response.put("delete successfully",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
