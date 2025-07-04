package com.fidelidad.servicio;

import com.fidelidad.modelo.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClienteServiceTest {

    private ClienteService servicio;

    @BeforeEach
    void setUp() {
        servicio = new ClienteService();
    }

    @Test
    void agregarCliente_valido_retornaTrueYSeGuarda() {
        boolean resultado = servicio.agregarCliente(1, "Ana", "ana@mail.com");
        assertTrue(resultado);

        Cliente cliente = servicio.buscarCliente(1);
        assertNotNull(cliente);
        assertEquals("Ana", cliente.getNombre());
        assertEquals("ana@mail.com", cliente.getCorreo());
        assertEquals(0, cliente.getPuntos());
    }

    // @Test
    // void agregarCliente_conCorreoInvalido_retornaFalse() {
    //     boolean resultado = servicio.agregarCliente(2, "Pedro", "pedro.com");
    //     assertFalse(resultado);
    //     assertNull(servicio.buscarCliente(2));
    // }

    // @Test
    // void agregarCliente_conIdDuplicado_retornaFalse() {
    //     servicio.agregarCliente(1, "Ana", "ana@mail.com");
    //     boolean resultado = servicio.agregarCliente(1, "Otra Ana", "otra@mail.com");
    //     assertFalse(resultado);

    //     Cliente cliente = servicio.buscarCliente(1);
    //     assertEquals("Ana", cliente.getNombre()); // No fue reemplazado
    // }

    // @Test
    // void actualizarCliente_existente_actualizaDatos() {
    //     servicio.agregarCliente(1, "Ana", "ana@mail.com");
    //     boolean resultado = servicio.actualizarCliente(1, "Ana Nueva", "nueva@mail.com");
    //     assertTrue(resultado);

    //     Cliente actualizado = servicio.buscarCliente(1);
    //     assertEquals("Ana Nueva", actualizado.getNombre());
    //     assertEquals("nueva@mail.com", actualizado.getCorreo());
    // }

    // @Test
    // void actualizarCliente_noExiste_retornaFalse() {
    //     boolean resultado = servicio.actualizarCliente(99, "X", "x@mail.com");
    //     assertFalse(resultado);
    // }

    // @Test
    // void eliminarCliente_existente_loElimina() {
    //     servicio.agregarCliente(1, "Ana", "ana@mail.com");
    //     boolean resultado = servicio.eliminarCliente(1);
    //     assertTrue(resultado);
    //     assertNull(servicio.buscarCliente(1));
    // }

    // @Test
    // void eliminarCliente_inexistente_retornaFalse() {
    //     boolean resultado = servicio.eliminarCliente(999);
    //     assertFalse(resultado);
    // }

    // @Test
    // void listarClientes_devuelveListaConClientesCreados() {
    //     servicio.agregarCliente(1, "Ana", "ana@mail.com");
    //     servicio.agregarCliente(2, "Pedro", "pedro@mail.com");

    //     List<Cliente> lista = servicio.listarClientes();
    //     assertEquals(2, lista.size());
    //     assertTrue(lista.stream().anyMatch(c -> c.getNombre().equals("Ana")));
    //     assertTrue(lista.stream().anyMatch(c -> c.getNombre().equals("Pedro")));
    // }
}
