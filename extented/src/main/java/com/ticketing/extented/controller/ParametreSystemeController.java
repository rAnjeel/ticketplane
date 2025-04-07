package com.ticketing.extented.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ticketing.extented.model.ParametreSysteme;
import com.ticketing.extented.service.ParametreSystemeService;

@Controller
@RequestMapping("/parametres")
public class ParametreSystemeController {

    @Autowired
    private ParametreSystemeService parametreSystemeService;
    
    @GetMapping
    public String listParametres(Model model) {
        List<ParametreSysteme> parametres = parametreSystemeService.getAllParametres();
        model.addAttribute("parametres", parametres);
        return "parametres/list";
    }
    
    @PostMapping("/update")
    public String updateParametre(
            @RequestParam("code") String code,
            @RequestParam("valeur") String valeur,
            RedirectAttributes redirectAttributes) {
        
        parametreSystemeService.updateParametre(code, valeur);
        redirectAttributes.addFlashAttribute("success", "Paramètre mis à jour avec succès!");
        return "redirect:/parametres";
    }
} 