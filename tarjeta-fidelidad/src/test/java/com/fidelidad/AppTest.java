package com.fidelidad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testObtenerNivelPorPuntos() {
        assertEquals("Bronce", App.obtenerNivelPorPuntos(0));
        assertEquals("Plata", App.obtenerNivelPorPuntos(500));
        assertEquals("Oro", App.obtenerNivelPorPuntos(2000));
        assertEquals("Platino", App.obtenerNivelPorPuntos(3000));
    }
}
