package com.ticketing.extented.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parametres_systeme")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametreSysteme {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "code", nullable = false, unique = true, length = 50, columnDefinition = "VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String code;
    
    @Column(name = "valeur", nullable = false, length = 255, columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String valeur;
    
    @Column(name = "description", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String description;
    
    @Column(name = "date_modification")
    private LocalDateTime dateModification;
} 