package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.Film;
import com.xuandanh.springbootshop.dto.FilmDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.mapper.FilmMapper;
import com.xuandanh.springbootshop.repository.FilmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private final FilmMapper filmMapper;
    private final FilmRepository filmRepository;
    private final Logger log = LoggerFactory.getLogger(FilmService.class);
    public FilmMapper getFilmMapper() {
        return filmMapper;
    }

    public FilmRepository getFilmRepository() {
        return filmRepository;
    }

    public FilmService(FilmMapper filmMapper, FilmRepository filmRepository){
        this.filmMapper = filmMapper;
        this.filmRepository = filmRepository;
    }

    public Optional<FilmDTO> findOne(String filmId){
        return Optional.ofNullable(filmMapper
                .filmToFilmDTO(filmRepository
                .findById(filmId)
                .orElseThrow(()->new ResourceNotFoundException("film with id:"+filmId+" not exist in system"))));
    }

    public Optional<List<FilmDTO>> findAll(){
        return Optional.ofNullable(filmMapper
                .filmToFilmDTO(filmRepository
                .findAll()));
    }

    public FilmDTO createFilm(Film film){
        return filmMapper.filmToFilmDTO(filmRepository.save(film));
    }

    public Optional<FilmDTO> updateFilm(Film film){
       return Optional.of(filmRepository
                .findById(film.getFilmId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(film1 -> {
                    film1.setFilmId(film.getFilmId());
                    film1.setTitle(film.getTitle());
                    film1.setDescription(film.getDescription());
                    film1.setLastUpdate(film.getLastUpdate());
                    film1.setRating(film.getRating());
                    film1.setReleaseYear(film.getReleaseYear());
                    film1.setRentalDuration(film.getRentalDuration());
                    film1.setRentalRate(film.getRentalRate());
                    film1.setLanguage(film.getLanguage());
                    film1.setImageUrl(film.getImageUrl());
                    return filmMapper.filmToFilmDTO(filmRepository.save(film1));
                });
    }

    public void deleteFilm(FilmDTO filmDTO){
        filmRepository.findById(filmDTO.getFilmId())
                    .ifPresent(film -> {
                        filmRepository.delete(film);
                        log.debug("Deleted Film: {}", film);
                    });
    }

    public List<Film> listAll() {
        return filmRepository.findAll(Sort.by("rating").ascending());
    }
}
