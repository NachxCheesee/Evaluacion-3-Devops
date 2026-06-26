package cl.marfuego.ms_locales.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class LocalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Cobertura para el endpoint de Locales
    @Test
    void ListarLocalesExitosamente() throws Exception {
        mockMvc.perform(get("/api/locales"))
                .andExpect(status().isOk()); // Verifica que devuelva un 200 OK (aunque la lista esté vacía)
    }

    // Cobertura para el endpoint de Mesas
    @Test
    void ListarMesasExitosamente() throws Exception {
        mockMvc.perform(get("/api/locales/mesas"))
                .andExpect(status().isOk()); // Verifica que el enrutamiento de mesas funcione limpio
    }

    // El validador del Freno de Mano Automatizado (Para el IE6)
    @Test
    void validadorFrenoPipeline() {
        int estadoControl = 1;
        // Estado normal : 1 es igual a 1 (Pasa en VERDE)
        // Si es 2 (Pasa a Rojo)
        assertEquals(1, estadoControl);
    }
}