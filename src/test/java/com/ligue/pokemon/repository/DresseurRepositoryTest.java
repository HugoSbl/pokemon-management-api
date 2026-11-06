package com.ligue.pokemon.repository;

import com.ligue.pokemon.model.Dresseur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // base H2 embarquée
class DresseurRepositoryTest {

    @Autowired
    private DresseurRepository dresseurRepository;

    @Test
    void should_load_all_dresseurs_when_data_sql_is_executed() {
        // Arrange : jeu de données chargé depuis data.sql

        // Act
        List<Dresseur> dresseurs = dresseurRepository.findAll();

        // Assert
        assertEquals(3, dresseurs.size());
    }

    @Test
    void should_return_dresseur_with_pokemons_when_name_exists() {
        // Arrange
        String nom = "Sacha";

        // Act
        Optional<Dresseur> resultat = dresseurRepository.findByNom(nom);

        // Assert
        assertTrue(resultat.isPresent());
        assertEquals("Kanto", resultat.get().getRegion());
        assertEquals(3, resultat.get().getPokemons().size());
    }

    @Test
    void should_return_empty_when_name_does_not_exist() {
        // Arrange
        String nom = "Inconnu";

        // Act
        Optional<Dresseur> resultat = dresseurRepository.findByNom(nom);

        // Assert
        assertTrue(resultat.isEmpty());
    }

    @Test
    void should_return_dresseurs_of_region_when_region_matches() {
        // Arrange
        String region = "Kanto";

        // Act
        List<Dresseur> resultat = dresseurRepository.findByRegion(region);

        // Assert
        assertEquals(2, resultat.size());
    }

    @Test
    void should_persist_dresseur_and_generate_id_when_saving_new_dresseur() {
        // Arrange
        Dresseur nouveau = new Dresseur(null, "Pierre", "Kanto");

        // Act
        Dresseur sauvegarde = dresseurRepository.save(nouveau);

        // Assert
        assertTrue(sauvegarde.getId() != null);
        assertEquals(4, dresseurRepository.count());
    }

    @Test
    void should_remove_dresseur_when_deleting_by_id() {
        // Arrange
        Long id = dresseurRepository.findByNom("Flora").orElseThrow().getId();

        // Act
        dresseurRepository.deleteById(id);

        // Assert
        assertTrue(dresseurRepository.findById(id).isEmpty());
        assertEquals(2, dresseurRepository.count());
    }
}
