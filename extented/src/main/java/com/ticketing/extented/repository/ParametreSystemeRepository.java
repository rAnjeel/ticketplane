package com.ticketing.extented.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketing.extented.model.ParametreSysteme;

@Repository
public interface ParametreSystemeRepository extends JpaRepository<ParametreSysteme, Long> {
    ParametreSysteme findByCode(String code);
} 