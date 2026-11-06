package com.ligue.pokemon.controller;

import com.ligue.pokemon.dto.DresseurDto;
import com.ligue.pokemon.exception.DresseurNotFoundException;
import com.ligue.pokemon.service.DresseurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DresseurController.class)
class DresseurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DresseurService dresseurService;

    @Test
    void should_return_200_and_dresseur_dto_when_dresseur_exists() throws Exception {
        // Arrange
        when(dresseurService.getDresseurDto(1L)).thenReturn(new DresseurDto("Sacha", 3, 120));

        // Act & Assert
        mockMvc.perform(get("/dresseurs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomDresseur").value("Sacha"))
                .andExpect(jsonPath("$.nombrePokemons").value(3))
                .andExpect(jsonPath("$.niveauDresseur").value(120));
    }

    @Test
    void should_return_404_when_dresseur_does_not_exist() throws Exception {
        // Arrange
        when(dresseurService.getDresseurDto(99L)).thenThrow(new DresseurNotFoundException(99L));

        // Act & Assert
        mockMvc.perform(get("/dresseurs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_400_when_id_is_not_a_number() throws Exception {
        // Arrange : aucun mock nécessaire, l'id est invalide

        // Act & Assert
        mockMvc.perform(get("/dresseurs/abc"))
                .andExpect(status().isBadRequest());
    }
}
