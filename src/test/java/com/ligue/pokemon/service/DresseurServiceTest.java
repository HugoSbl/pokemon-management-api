package com.ligue.pokemon.service;

import com.ligue.pokemon.dto.DresseurDto;
import com.ligue.pokemon.exception.DresseurNotFoundException;
import com.ligue.pokemon.model.Dresseur;
import com.ligue.pokemon.model.Pokemon;
import com.ligue.pokemon.repository.DresseurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DresseurServiceTest {

    @Mock
    private DresseurRepository dresseurRepository;

    @InjectMocks
    private DresseurService dresseurService;

    private Dresseur dresseurAvec(Pokemon... pokemons) {
        Dresseur dresseur = new Dresseur(1L, "Sacha", "Kanto");
        dresseur.setPokemons(List.of(pokemons));
        return dresseur;
    }

    private Pokemon pokemon(String type, int niveau) {
        return new Pokemon(null, "Pokemon", type, niveau);
    }

    @Test
    void should_return_sum_of_levels_when_no_bonus_applies() {
        // Arrange
        Dresseur dresseur = dresseurAvec(pokemon("Feu", 10), pokemon("Eau", 20));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(30, niveau);
    }

    @Test
    void should_return_level_1_when_dresseur_has_no_pokemon() {
        // Arrange
        Dresseur dresseur = dresseurAvec();
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(1, niveau);
    }

    @Test
    void should_apply_diversity_bonus_when_dresseur_has_3_different_types() {
        // Arrange
        Dresseur dresseur = dresseurAvec(pokemon("Feu", 10), pokemon("Eau", 10), pokemon("Électrik", 10));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(30 + 20, niveau);
    }

    @Test
    void should_not_apply_diversity_bonus_when_dresseur_has_only_2_different_types() {
        // Arrange
        Dresseur dresseur = dresseurAvec(pokemon("Feu", 10), pokemon("Eau", 10), pokemon("Eau", 10));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(30, niveau);
    }

    @Test
    void should_apply_elite_bonus_when_pokemon_level_is_exactly_50() {
        // Arrange
        Dresseur dresseur = dresseurAvec(pokemon("Feu", 50));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(50 + 10, niveau);
    }

    @Test
    void should_not_apply_elite_bonus_when_pokemon_level_is_49() {
        // Arrange
        Dresseur dresseur = dresseurAvec(pokemon("Feu", 49));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(49, niveau);
    }

    @Test
    void should_apply_one_elite_bonus_per_pokemon_when_several_pokemons_are_elite() {
        // Arrange
        Dresseur dresseur = dresseurAvec(pokemon("Feu", 60), pokemon("Feu", 70), pokemon("Feu", 5));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(135 + 2 * 10, niveau);
    }

    @Test
    void should_cumulate_all_bonuses_when_diversity_and_elite_conditions_are_met() {
        // Arrange : somme = 50 + 80 + 10 = 140 ; diversité +20 ; 2 élites +20
        Dresseur dresseur = dresseurAvec(pokemon("Feu", 50), pokemon("Eau", 80), pokemon("Électrik", 10));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(140 + 20 + 20, niveau);
    }

    @Test
    void should_throw_exception_when_dresseur_does_not_exist() {
        // Arrange
        when(dresseurRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        DresseurNotFoundException exception = assertThrows(DresseurNotFoundException.class,
                () -> dresseurService.calculerNiveauDresseur(99L));

        // Assert
        assertEquals("Dresseur introuvable avec l'id 99", exception.getMessage());
    }

    @Test
    void should_return_dto_with_name_count_and_level_when_dresseur_exists() {
        // Arrange
        Dresseur dresseur = dresseurAvec(pokemon("Feu", 50), pokemon("Eau", 10));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        DresseurDto dto = dresseurService.getDresseurDto(1L);

        // Assert
        assertEquals("Sacha", dto.nomDresseur());
        assertEquals(2, dto.nombrePokemons());
        assertEquals(60 + 10, dto.niveauDresseur());
    }
}
