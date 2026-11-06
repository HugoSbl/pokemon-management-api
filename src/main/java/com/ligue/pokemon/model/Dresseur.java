package com.ligue.pokemon.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Dresseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String region;

    @OneToMany(mappedBy = "dresseur", cascade = CascadeType.ALL)
    private List<Pokemon> pokemons = new ArrayList<>();

    public Dresseur() {
    }

    public Dresseur(Long id, String nom, String region) {
        this.id = id;
        this.nom = nom;
        this.region = region;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public List<Pokemon> getPokemons() { return pokemons; }
    public void setPokemons(List<Pokemon> pokemons) { this.pokemons = pokemons; }
}
