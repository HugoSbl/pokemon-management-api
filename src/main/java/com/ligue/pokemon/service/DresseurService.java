package com.ligue.pokemon.service;

import com.ligue.pokemon.dto.DresseurDto;
import com.ligue.pokemon.exception.DresseurNotFoundException;
import com.ligue.pokemon.model.Dresseur;
import com.ligue.pokemon.model.Pokemon;
import com.ligue.pokemon.repository.DresseurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DresseurService {

    static final int NIVEAU_SANS_POKEMON = 1;
    static final int SEUIL_TYPES_DISTINCTS = 3;
    static final int BONUS_DIVERSITE = 20;
    static final int SEUIL_ELITE = 50;
    static final int BONUS_ELITE = 10;

    private final DresseurRepository dresseurRepository;

    public DresseurService(DresseurRepository dresseurRepository) {
        this.dresseurRepository = dresseurRepository;
    }

    public int calculerNiveauDresseur(Long dresseurId) {
        return calculerNiveau(trouverDresseur(dresseurId));
    }

    public DresseurDto getDresseurDto(Long dresseurId) {
        Dresseur dresseur = trouverDresseur(dresseurId);
        return new DresseurDto(
                dresseur.getNom(),
                dresseur.getPokemons().size(),
                calculerNiveau(dresseur));
    }

    private Dresseur trouverDresseur(Long dresseurId) {
        return dresseurRepository.findById(dresseurId)
                .orElseThrow(() -> new DresseurNotFoundException(dresseurId));
    }

    private int calculerNiveau(Dresseur dresseur) {
        List<Pokemon> pokemons = dresseur.getPokemons();
        if (pokemons.isEmpty()) {
            return NIVEAU_SANS_POKEMON;
        }

        int niveau = pokemons.stream().mapToInt(Pokemon::getNiveau).sum();

        long typesDistincts = pokemons.stream().map(Pokemon::getType).distinct().count();
        if (typesDistincts >= SEUIL_TYPES_DISTINCTS) {
            niveau += BONUS_DIVERSITE;
        }

        long nombreElites = pokemons.stream().filter(p -> p.getNiveau() >= SEUIL_ELITE).count();
        niveau += (int) nombreElites * BONUS_ELITE;

        return niveau;
    }
}
