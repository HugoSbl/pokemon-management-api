package com.ligue.pokemon.controller;

import com.ligue.pokemon.dto.DresseurDto;
import com.ligue.pokemon.service.DresseurService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DresseurController {

    private final DresseurService dresseurService;

    public DresseurController(DresseurService dresseurService) {
        this.dresseurService = dresseurService;
    }

    @GetMapping("/dresseurs/{id}")
    public DresseurDto getDresseur(@PathVariable Long id) {
        return dresseurService.getDresseurDto(id);
    }
}
