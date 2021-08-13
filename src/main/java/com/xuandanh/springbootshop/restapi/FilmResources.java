package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.Film;
import com.xuandanh.springbootshop.dto.FilmDTO;
import com.xuandanh.springbootshop.exception.MyResourceNotFoundException;
import com.xuandanh.springbootshop.repository.FilmRepository;
import com.xuandanh.springbootshop.service.FilmService;
import com.xuandanh.springbootshop.util.FilmExcelExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class FilmResources {
    private final FilmService filmService;
    private final FilmRepository filmRepository;
    private final Logger log = LoggerFactory.getLogger(FilmResources.class);

    public FilmService getFilmService() {
        return filmService;
    }

    public FilmRepository getFilmRepository() {
        return filmRepository;
    }

    public FilmResources(FilmService filmService, FilmRepository filmRepository){
        this.filmRepository = filmRepository;
        this.filmService = filmService;
    }

    @GetMapping("/film/findOne/{filmId}")
    public ResponseEntity<?>getOne(@PathVariable("filmId")String filmId){
        Optional<FilmDTO>filmDTO = filmService.findOne(filmId);
        if(filmDTO.isEmpty()){
            log.error("film with id:"+filmId+" not exist in system");
            throw new MyResourceNotFoundException("film with id:"+filmId+" not exist in system");
        }
        else
        {
            log.info("show information of a film with id:"+filmId+filmDTO.get());
            return ResponseEntity.ok(filmDTO);
        }
    }

    @GetMapping("/film/findAll")
    public ResponseEntity getAll(){
        Optional<List<FilmDTO>>filmDTOList = filmService.findAll();
        try{
            if(filmDTOList.stream().count()>0){
                log.info("show list film"+filmDTOList);
                return ResponseEntity.ok(filmDTOList);
            }
        }catch (MyResourceNotFoundException e){
            log.error(e.getMessage());
            throw new MyResourceNotFoundException("list film empty");
        }
        return ResponseEntity.ok(Optional.empty());
    }

    @PostMapping("/film/createFilm")
    public ResponseEntity create(@RequestBody @Valid Film film){
        return ResponseEntity.ok().body(filmService.createFilm(film));
    }

    @PutMapping("/film/filmUpdate/{filmId}")
    public ResponseEntity update(@PathVariable("filmId")String filmId,@Valid @RequestBody Film film){
        Optional<FilmDTO>filmDTO = filmService.findOne(filmId);
        try{
            if(filmDTO.isPresent()){
                log.info("film with id:"+filmId+" was update successfully");
                return ResponseEntity.ok(filmService.updateFilm(film));
            }
            return ResponseEntity.ok(new FilmDTO());
        }catch (MyResourceNotFoundException e){
            log.error("film with id:"+filmId+" not exist in system");
            throw new MyResourceNotFoundException("film with id:"+filmId+" not exist in system");
        }
    }

    @DeleteMapping("/film/deleteFilm/{filmId}")
    public ResponseEntity delete(@PathVariable("filmId")String filmId){
        Optional<FilmDTO>filmDTO = filmService.findOne(filmId);
        Map<String, Boolean>response = new HashMap<>();
        try{
            if (filmDTO.isPresent()){
                filmService.deleteFilm(filmDTO.get());
                log.info("film with id:"+filmId+" was deleted successfully");
                response.put("film with id:"+filmId+" was deleted successfully",Boolean.TRUE);
                return ResponseEntity.ok(response);
            }
        }catch (MyResourceNotFoundException exception){
            log.error("film with id:"+filmId + " not exist system");
            throw new MyResourceNotFoundException("film with id:"+filmId+" not found");
        }
        return ResponseEntity.ok(Optional.empty());
    }

    @GetMapping("/films/findFilmByTitle")
    public ResponseEntity<Map<String, Object>> getFilmByTitle(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        try {
            List<Film> films = new ArrayList<Film>();
            Pageable paging = PageRequest.of(page, size);

            Page<Film> pageTuts;
            if (title == null)
                pageTuts = filmRepository.findAll(paging);
            else
                pageTuts = filmRepository.findByTitle(title, paging);

            films = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("films", films);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/film/findByTitlePage")
    public ResponseEntity<Map<String, Object>> getFilmByTitlePage(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        try {
            List<Order> orders = new ArrayList<Order>();

            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
            }

            List<Film> films = new ArrayList<Film>();
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

            Page<Film> pageTuts;
            if (title == null)
                pageTuts = filmRepository.findAll(pagingSort);
            else
                pageTuts = filmRepository.findByTitle(title, pagingSort);

            films = pageTuts.getContent();

            if (films.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("films", films);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    @GetMapping("/films/findFilmByRentalRate")
    public ResponseEntity<Map<String, Object>> getFilmByRentalRate(
            @RequestParam(required = false) String rentalRate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        try {
            List<Film> films = new ArrayList<Film>();
            Pageable paging = PageRequest.of(page, size);

            Page<Film> pageTuts;
            if (rentalRate == null)
                pageTuts = filmRepository.findAll(paging);
            else
                pageTuts = filmRepository.findByRentalRate(rentalRate, paging);

            films = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("films", films);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/film/findByRentalRatePage")
    public ResponseEntity<Map<String, Object>> getFilmByRentalRatePage(
            @RequestParam(required = false) String rentalRate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        try {
            List<Order> orders = new ArrayList<Order>();

            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
            }

            List<Film> films = new ArrayList<Film>();
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

            Page<Film> pageTuts;
            if (rentalRate == null)
                pageTuts = filmRepository.findAll(pagingSort);
            else
                pageTuts = filmRepository.findByRentalRate(rentalRate, pagingSort);

            films = pageTuts.getContent();

            if (films.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("films", films);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/film/getFilmBySortRating")
    public ResponseEntity getFilmBySortRating(){
        // order by 'rating' column, descending
        List<Film> films = filmRepository.findAll(Sort.by("rating").descending());
        return ResponseEntity.ok(films);
    }


    @GetMapping("/film/getFilmBySortRentalRate")
    public ResponseEntity getFilmBySortRentalRate(){
        // order by 'rentalrate' column, descending
        List<Film> films = filmRepository.findAll(Sort.by("rentalRate").descending());
        return ResponseEntity.ok(films);
    }

    @GetMapping("/films/export/films.xlsx")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=films.xlsx");

        List<Film> filmList = filmService.listAll();

        FilmExcelExporter excelExporter = new FilmExcelExporter(filmList);

        excelExporter.export(response);
    }
}
