package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.Language;
import com.xuandanh.springbootshop.dto.LanguageDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.mapper.LanguageMapper;
import com.xuandanh.springbootshop.repository.LanguageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;
    private final Logger log = LoggerFactory.getLogger(LanguageService.class);
    public LanguageRepository getLanguageRepository() {
        return languageRepository;
    }

    public LanguageMapper getLanguageMapper() {
        return languageMapper;
    }

    public LanguageService(LanguageRepository languageRepository, LanguageMapper languageMapper){
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
    }

    public Optional<LanguageDTO>findOne(int languageId){
        return Optional.ofNullable(languageMapper.languageToLanguageDTO(languageRepository.findById(languageId).orElseThrow(()->new ResourceNotFoundException("language:"+languageId+" not exist"))));
    }

    public Optional<List<LanguageDTO>>findAll(){
        List<Language>languageList = languageRepository.findAll();
        try{
            if (languageList.get(0)!=null){
                log.info("show list language in system");
                return Optional.ofNullable(languageMapper.languageToLanguageDTO(languageRepository.findAll()));
            }
        }catch (Exception e){
            log.error("List language not exist in system");
            throw new ResourceNotFoundException("List language not exist:");
        }
        return Optional.ofNullable(languageMapper.languageToLanguageDTO(languageRepository.findAll()));
    }

    public Optional<LanguageDTO>createLanguage(Language language){
        try{
            if(language.getLanguageName()!=null){
                log.info("create language successfully");
                return Optional.ofNullable(languageMapper.languageToLanguageDTO(languageRepository.save(language)));
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("create language faild");
            throw new ResourceNotFoundException("this language already exist in system");
        }
        return Optional.ofNullable(languageMapper.languageToLanguageDTO(languageRepository.save(language)));
    }

    public Optional<LanguageDTO>updateLanguage(Language language){
        return Optional.of(languageRepository
                .findById(language.getLanguageId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(language1 -> {
                    language1.setLanguageId(language.getLanguageId());
                    language1.setLanguageName(language.getLanguageName());
                    language1.setLastUpdate(language.getLastUpdate());
                    log.info("update information language successfully"+language);
                    return languageMapper.languageToLanguageDTO(languageRepository.save(language1));
                });
    }

    public void deleteLanguage(LanguageDTO language){
        languageRepository.findById(language.getLanguageId())
                        .ifPresent(languageRepository::delete);
    }
}
