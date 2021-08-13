package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.Language;
import com.xuandanh.springbootshop.dto.LanguageDTO;
import com.xuandanh.springbootshop.repository.LanguageRepository;
import com.xuandanh.springbootshop.service.LanguageService;
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
public class LanguageResources {
    private final Logger log = LoggerFactory.getLogger(LanguageResources.class);
    private final LanguageService languageService;
    private final LanguageRepository languageRepository;

    public LanguageService getLanguageService() {
        return languageService;
    }

    public LanguageRepository getLanguageRepository() {
        return languageRepository;
    }

    public LanguageResources(LanguageService languageService, LanguageRepository languageRepository){
        this.languageService = languageService;
        this.languageRepository = languageRepository;
    }

    @GetMapping("/language/findOne/{languageId}")
    public ResponseEntity<?>getOne(@PathVariable("languageId")int languageId){
        Optional<LanguageDTO>languageDTO = languageService.findOne(languageId);
        try{
            if (languageDTO.isPresent()){
                log.info("Get information of an language with id:"+languageId);
                return ResponseEntity.ok(languageDTO.get());
            }
        }catch (Exception e){
            log.error("this language with id "+languageId+" not exist in system");
        }
        return ResponseEntity.ok(new LanguageDTO());
    }

    @GetMapping("/languages/findAll")
    public ResponseEntity<?>getAll(){
        Optional<List<LanguageDTO>>languageDTOList = languageService.findAll();
        if(languageDTOList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(languageDTOList);
    }

    @PostMapping("/language/createLanguage")
    public ResponseEntity<?>create(@Valid @RequestBody Language language){
        Optional<LanguageDTO>languageDTO = languageService.createLanguage(language);
        if(languageDTO.isEmpty()){
            return ResponseEntity.ok(new LanguageDTO());
        }
        return ResponseEntity.ok(languageService.createLanguage(language));
    }

    @PutMapping("/language/updateLanguage/{languageId}")
    public ResponseEntity<?>update(@PathVariable("languageId")int languageId,@Valid @RequestBody Language language){
        Optional<LanguageDTO>languageDTO = languageService.findOne(languageId);
        if(languageDTO.isEmpty()){
            log.error("language with languageId:"+languageId+" not exist system");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(languageService.updateLanguage(language));
    }

    @DeleteMapping("language/delete/{languageId}")
    public ResponseEntity<?>delete(@PathVariable("languageId")int languageId){
        Optional<LanguageDTO>languageDTO = languageService.findOne(languageId);
        Map<String,Boolean> response = new HashMap<>();
        if(languageDTO.isEmpty()){
            log.error("language with id:"+languageId+" not exist in system");
            response.put("language with id:"+languageId+" not exist ",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        languageService.deleteLanguage(languageDTO.get());
        log.info("language with id:"+languageId+" was delete successfully");
        response.put("language with id:"+languageId+" was delete successfully",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
