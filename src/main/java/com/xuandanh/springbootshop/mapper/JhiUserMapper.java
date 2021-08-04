package com.xuandanh.springbootshop.mapper;

import com.xuandanh.springbootshop.domain.Authority;
import com.xuandanh.springbootshop.domain.JhiUser;
import com.xuandanh.springbootshop.dto.JhiUserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link com.xuandanh.springbootshop.domain.JhiUser} and its DTO called {@link com.xuandanh.springbootshop.dto.JhiUserDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class JhiUserMapper {
    public List<JhiUserDTO>jhiUserDTOToJhiUser(List<JhiUser> jhiUserList){
        return jhiUserList.stream().filter(Objects::nonNull).map(this::jhiUserDTOToJhiUser).collect(Collectors.toList());
    }

    public JhiUserDTO jhiUserDTOToJhiUser(JhiUser jhiUser){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(jhiUser,JhiUserDTO.class);
    }

    public List<JhiUser>jhiUserToJhiUserDTO(List<JhiUserDTO>jhiUserDTOList){
        return jhiUserDTOList.stream().filter(Objects::nonNull).map(this::jhiUserToJhiUserDTO).collect(Collectors.toList());
    }

    public JhiUser jhiUserToJhiUserDTO(JhiUserDTO jhiUserDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(jhiUserDTO,JhiUser.class);
    }

    private Set<Authority>authoritiesFromStrings(Set<String> authoritiesAsString){
        Set<Authority>authorities = new HashSet<>();
        if(authoritiesAsString!=null){
            authorities = authoritiesAsString.stream().map(string->{
                Authority authority = new Authority();
                authority.setName(string);
                return authority;
            }).collect(Collectors.toSet());
        }
        return authorities;
    }

    public JhiUser jhiUserFromId(Long id){
        if(id==null){
            return null;
        }
        JhiUser jhiUser = new JhiUser();
        jhiUser.setId(id);
        return jhiUser;
    }
}
