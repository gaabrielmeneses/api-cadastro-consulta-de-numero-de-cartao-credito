package com.infrastructure.repository.mysql.repository;

import com.infrastructure.repository.mysql.Entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardJpaRepository extends JpaRepository<CardEntity, UUID> {
    Optional<CardEntity> findByCardHash(String cardHash);
}