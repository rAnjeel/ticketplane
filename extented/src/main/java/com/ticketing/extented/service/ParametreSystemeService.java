package com.ticketing.extented.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketing.extented.model.ParametreSysteme;
import com.ticketing.extented.repository.ParametreSystemeRepository;

@Service
public class ParametreSystemeService {

    @Autowired
    private ParametreSystemeRepository parametreSystemeRepository;
    
    public List<ParametreSysteme> getAllParametres() {
        return parametreSystemeRepository.findAll();
    }
    
    public ParametreSysteme getParametreByCode(String code) {
        return parametreSystemeRepository.findByCode(code);
    }
    
    public ParametreSysteme saveParametre(ParametreSysteme parametre) {
        parametre.setDateModification(LocalDateTime.now());
        return parametreSystemeRepository.save(parametre);
    }
    
    public void updateParametre(String code, String valeur) {
        ParametreSysteme parametre = getParametreByCode(code);
        if (parametre != null) {
            parametre.setValeur(valeur);
            parametre.setDateModification(LocalDateTime.now());
            parametreSystemeRepository.save(parametre);
        }
    }
} 