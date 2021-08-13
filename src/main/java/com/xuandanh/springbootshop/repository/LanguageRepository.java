package com.xuandanh.springbootshop.repository;

import com.xuandanh.springbootshop.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language,Integer> {
//    Language findByLanguageId(int languageId);
}
