package com.sbsc_fcmb.sample_app.repository;

import com.sbsc_fcmb.sample_app.model.SystemUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface SystemUserRepository extends JpaRepository<SystemUserEntity, Long>, QuerydslPredicateExecutor<SystemUserEntity> {
    Optional<SystemUserEntity> findByUsernameOrEmail(String username, String email);
    Optional<SystemUserEntity> findByUsername(String username);
    Optional<SystemUserEntity> findByEmail(String email);
    Optional<SystemUserEntity> findByCode(String code);
    boolean existsByUsernameOrEmail(String username, String email);
}
