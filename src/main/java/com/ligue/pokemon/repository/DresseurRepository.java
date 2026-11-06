package com.ligue.pokemon.repository;

import com.ligue.pokemon.model.Dresseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DresseurRepository extends JpaRepository<Dresseur, Long> {

    Optional<Dresseur> findByNom(String nom);

    List<Dresseur> findByRegion(String region);
}
