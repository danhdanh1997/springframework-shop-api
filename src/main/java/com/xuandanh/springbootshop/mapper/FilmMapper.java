package com.xuandanh.springbootshop.mapper;

import com.xuandanh.springbootshop.domain.Film;
import com.xuandanh.springbootshop.dto.FilmDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class FilmMapper {
    public List<FilmDTO> filmToFilmDTO(List<Film> filmList) {
        return filmList.stream().filter(Objects::nonNull).map(this::filmToFilmDTO).collect(Collectors.toList());
    }

    public FilmDTO filmToFilmDTO(Film film){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(film,FilmDTO.class);
    }

    public List<Film> filmDTOToFilm(List<FilmDTO> filmDTOList) {
        return filmDTOList.stream().filter(Objects::nonNull).map(this::filmDTOToFilm).collect(Collectors.toList());
    }

    public Film filmDTOToFilm(FilmDTO filmDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(filmDTO , Film.class);
    }
}
