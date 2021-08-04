package com.xuandanh.springbootshop.mapper;

import com.xuandanh.springbootshop.domain.Actor;
import com.xuandanh.springbootshop.dto.ActorDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class ActorMapper {
    public List<ActorDTO> actorToactorDTOs(List<Actor> actorList) {
        return actorList.stream().filter(Objects::nonNull).map(this::actorToactorDTO).collect(Collectors.toList());
    }

    public ActorDTO actorToactorDTO(Actor actor){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(actor,ActorDTO.class);
    }

    public List<Actor> actorDTOsToActor(List<ActorDTO> actorDTOList) {
        return actorDTOList.stream().filter(Objects::nonNull).map(this::actorDTOToActor).collect(Collectors.toList());
    }

    public Actor actorDTOToActor(ActorDTO actorDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(actorDTO , Actor.class);
    }

}
