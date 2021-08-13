package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.Actor;
import com.xuandanh.springbootshop.dto.ActorDTO;
import com.xuandanh.springbootshop.pagination.ResponseActor;
import com.xuandanh.springbootshop.repository.ActorRepository;
import com.xuandanh.springbootshop.service.ActorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ActorResources {
    private final ActorService actorService;
    private final ActorRepository actorRepository;
    private final Logger log = LoggerFactory.getLogger(ActorResources.class);
    public ActorService getActorService() {
        return actorService;
    }

    public ActorResources(ActorService actorService,ActorRepository actorRepository){
        this.actorService = actorService;
        this.actorRepository = actorRepository;
    }

    @GetMapping("/actor/{actorId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getOne(@PathVariable("actorId")String actorId){
        Optional<ActorDTO>actor = actorService.findOne(actorId);
        if(actor.isEmpty()){
            log.error("actor:"+actorId +" not exist");
            return ResponseEntity.notFound().build();
        }
        log.info("show information of actor with actorId:"+actorId);
        return ResponseEntity.ok(actor.get());
    }

    @GetMapping("/actors")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ActorDTO>>getAll(){
       List<ActorDTO>actorDTOList = actorService.findAll();
       if(actorDTOList.isEmpty()){
           log.error("list actor not exist");
           return ResponseEntity.notFound().build();
       }
       log.info("show list information of actor");
       return ResponseEntity.ok(actorDTOList);
    }

    @PostMapping("/actor")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@Valid @RequestBody Actor actor){
        ActorDTO actorDTO = actorService.createActor(actor);
        if(actorDTO==null){
            log.error("Must fill in Information for Actor: {}", actor);
            return (ResponseEntity) ResponseEntity.noContent();
        }
        log.info("Created Information for Actor: {}", actor);
        return ResponseEntity.ok().body(actorDTO);
    }

    @PutMapping("/actor/{actorId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity update(@PathVariable("actorId")String actorId,@RequestBody Actor actor){
        Optional<ActorDTO>actor1 = actorService.findOne(actorId);
        if(actor1.isEmpty()){
            log.error("actor: {}"+actorId+" not exist");
            return ResponseEntity.notFound().build();
        }
        log.info("Changed Information for Actor: {}", actor);
        return ResponseEntity.ok(actorService.updateActor(actor));
    }

    @DeleteMapping("/actor/{actorId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Map<String,Boolean>>delete(@PathVariable("actorId")String actorId){
        Optional<ActorDTO>actorDTO = actorService.findOne(actorId);
        Map<String,Boolean>response = new HashMap<>();
        if(actorDTO.isEmpty()){
            log.error("actor: {}"+actorId+" not exist");
            response.put("actor:"+actorId+" not found",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        actorService.deleteActor(actorDTO.get());
        log.info("actor: {}"+actorId+" was deleted successfully");
        response.put("delete successfully",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/actor/pageable")
    public Page<Actor> retrieveActorWithPaging(@Param(value = "page") int page,
                                               @Param(value = "size") int size){
        Pageable requestedPage = PageRequest.of(page, size);
        return actorRepository.findAll(requestedPage);
    }

    @GetMapping("/actor/custom/pageable")
    public ResponseActor retrieveActor(@Param(value = "page") int page,
                                       @Param(value = "size") int size){
        Pageable requestedPage = PageRequest.of(page, size);
        Page<Actor> actorPage  = actorRepository.findAll(requestedPage);
        return new ResponseActor(actorPage.getContent(), actorPage.getTotalPages(), actorPage.getNumber(), actorPage.getSize());
    }

    @GetMapping("/actor/pageable/list")
    public List<Actor>retrieveActorListWithPaging(@Param(value = "page") int page,
                                                  @Param(value = "size") int size){
        Pageable requestedPage = PageRequest.of(page, size);
        Page<Actor>actorPage = actorRepository.findAll(requestedPage);
        return actorPage.toList();
    }

    @GetMapping("/actor/retrieveActorByLastName")
    public Slice<Actor> retrieveActorByLastNameWithPaging(@Param(value = "lastName") String lastName,
                                                          @Param(value = "page") int page,
                                                          @Param(value = "size") int size){
        Pageable requestedPage = PageRequest.of(page, size);
        return actorRepository.findAllByLastName(lastName, requestedPage);
    }

    @GetMapping("/actor/pagingandsorting")
    public Page<Actor> pagingAndSortingCustomers(@Param(value = "page") int page,
                                                 @Param(value = "size") int size){
        Pageable requestedPage = PageRequest.of(page, size, Sort.by("lastName").descending());
        return actorRepository.findAll(requestedPage);
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    @GetMapping("/actor/sorted")
    public ResponseEntity<List<Actor>> getAllActors(@RequestParam(value = "actorId,desc") String[] sort) {
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
            List<Actor> actorList = actorRepository.findAll(Sort.by(orders));
            if (actorList.isEmpty()) {
                log.error("list actor not exist");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(actorList, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/actor/getAllActorPageByLastName")
    public ResponseEntity<Map<String, Object>> getAllActorPageByLastName(
            @RequestParam(required = false) String lastName,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "actorId,desc") String[] sort) {
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
            List<Actor> actorList = new ArrayList<Actor>();
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
            Page<Actor> pageTuts;
            if (lastName == null)
                pageTuts = actorRepository.findAll(pagingSort);
            else
                pageTuts = actorRepository.findByLastName(lastName, pagingSort);
            actorList = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("actorList", actorList);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            log.info("show page of list actor:"+response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/actor/getAllActorsByLastName")
    public  ResponseEntity<Map<String, Object>> getAllActors( @RequestParam(required = false) String lastName,
                                                              @RequestParam(value = "page") int page,
                                                              @RequestParam(value = "size") int size){
        try {
            List<Actor> actorList = new ArrayList<Actor>();
            Pageable paging = PageRequest.of(page, size);

            Page<Actor> pageTuts;
            if (lastName == null)
                pageTuts = actorRepository.findAll(paging);
            else
                pageTuts = actorRepository.findByLastName(lastName, paging);

            actorList = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("actorList", actorList);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            log.info("show page of list actor:"+response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/actor/getAllActorsByFirstName")
    public  ResponseEntity<Map<String, Object>> getAllActorsByFirstName( @RequestParam(required = false) String firstName,
                                                              @RequestParam(value = "page") int page,
                                                              @RequestParam(value = "size") int size){
        try {
            List<Actor> actorList = new ArrayList<Actor>();
            Pageable paging = PageRequest.of(page, size);

            Page<Actor> pageTuts;
            if (firstName == null)
                pageTuts = actorRepository.findAll(paging);
            else
                pageTuts = actorRepository.findByFirstName(firstName, paging);

            actorList = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("actorList", actorList);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            log.info("show page of list actor:"+response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/actor/findByLastNameOrderByFirstName")
    public  ResponseEntity<Map<String, Object>>findByFirstNameOrderByLastName(@RequestParam(value = "lastName")String lastName,
                                                                              @RequestParam(value = "page") int page,
                                                                              @RequestParam(value = "size") int size){
        try{
            List<Actor> actorList = new ArrayList<Actor>();
            Pageable paging = PageRequest.of(page, size);
            Page<Actor> pageTuts;
            if(lastName==null){
                pageTuts = actorRepository.findAll(paging);
            }
            else{
                pageTuts = actorRepository.findByLastNameOrderByFirstName(lastName, paging);
            }
            actorList = pageTuts.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("actorList", actorList);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            log.info("show page of list actor:"+response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
