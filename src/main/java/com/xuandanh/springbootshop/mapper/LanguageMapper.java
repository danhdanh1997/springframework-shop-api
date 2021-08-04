package com.xuandanh.springbootshop.mapper;

import com.xuandanh.springbootshop.domain.Language;
import com.xuandanh.springbootshop.dto.LanguageDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class LanguageMapper {
    public List<LanguageDTO> languageToLanguageDTO(List<Language> languageList) {
        return languageList.stream().filter(Objects::nonNull).map(this::languageToLanguageDTO).collect(Collectors.toList());
    }

    public LanguageDTO languageToLanguageDTO(Language language){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(language,LanguageDTO.class);
    }

    public List<Language> languageDTOToLanguage(List<LanguageDTO> languageDTOList) {
        return languageDTOList.stream().filter(Objects::nonNull).map(this::languageDTOToLanguage).collect(Collectors.toList());
    }

    public Language languageDTOToLanguage(LanguageDTO languageDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(languageDTO , Language.class);
    }
}
