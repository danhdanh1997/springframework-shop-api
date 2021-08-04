package com.xuandanh.springbootshop.repository;
import com.xuandanh.springbootshop.domain.JhiUser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data JPA repository for the {@link JhiUser} entity.
 */
@Repository
public interface JhiUserRepository extends JpaRepository<JhiUser, Long>{
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<JhiUser> findOneByActivationKey(String activationKey);

    List<JhiUser> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    Optional<JhiUser> findOneByResetKey(String resetKey);

    Optional<JhiUser> findOneByEmailIgnoreCase(String email);

    Optional<JhiUser> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<JhiUser> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<JhiUser> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<JhiUser> findOneWithAuthoritiesByEmail(String email);

    Page<JhiUser> findAllByLoginNot(Pageable pageable, String login);
}
