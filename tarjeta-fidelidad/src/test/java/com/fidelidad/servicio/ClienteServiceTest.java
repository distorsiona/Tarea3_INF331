package com.fidelidad.servicio;

import com.fidelidad.modelo.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClienteServiceTest {

    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        clienteService = new ClienteService();
    }

    // --- Tests para agregarCliente ---
    @Test
    void agregarCliente_valido_debeAgregarCorrectamente() {
        boolean resultado = clienteService.agregarCliente(1, "Juan", "juan@example.com");
        assertTrue(resultado);
        Cliente cliente = clienteService.buscarCliente(1);
        assertNotNull(cliente);
        assertEquals("Juan", cliente.getNombre());
    }

    @Test
    void agregarCliente_nombreVacio_debeFallar() {
        boolean resultado = clienteService.agregarCliente(2, "   ", "correo@example.com");
        assertFalse(resultado);
    }

    @Test
    void agregarCliente_correoInvalido_debeFallar() {
        boolean resultado = clienteService.agregarCliente(3, "Ana", "correo_invalido");
        assertFalse(resultado);
    }

    @Test
    void agregarCliente_idRepetido_debeFallar() {
        clienteService.agregarCliente(4, "Luis", "luis@example.com");
        boolean resultado = clienteService.agregarCliente(4, "Luis 2", "luis2@example.com");
        assertFalse(resultado);
    }

    // --- Tests para buscarCliente ---
    @Test
    void buscarCliente_existente_debeRetornarCliente() {
        clienteService.agregarCliente(5, "Carlos", "carlos@example.com");
        Cliente cliente = clienteService.buscarCliente(5);
        assertNotNull(cliente);
        assertEquals("Carlos", cliente.getNombre());
    }

    @Test
    void buscarCliente_inexistente_debeRetornarNull() {
        Cliente cliente = clienteService.buscarCliente(999);
        assertNull(cliente);
    }

    // --- Tests para listarClientes ---
    @Test
    void listarClientes_debeRetornarTodosLosClientes() {
        clienteService.agregarCliente(6, "A", "a@a.com");
        clienteService.agregarCliente(7, "B", "b@b.com");
        List<Cliente> lista = clienteService.listarClientes();
        assertEquals(2, lista.size());
    }

    // --- Tests para actualizarCliente ---
    @Test
    void actualizarCliente_existente_debeActualizar() {
        clienteService.agregarCliente(8, "Original", "orig@example.com");
        boolean actualizado = clienteService.actualizarCliente(8, "Nuevo", "nuevo@example.com");
        assertTrue(actualizado);
        Cliente cliente = clienteService.buscarCliente(8);
        assertEquals("Nuevo", cliente.getNombre());
    }

    @Test
    void actualizarCliente_inexistente_debeFallar() {
        boolean resultado = clienteService.actualizarCliente(999, "X", "x@example.com");
        assertFalse(resultado);
    }

    // --- Tests para eliminarCliente ---
    @Test
    void eliminarCliente_existente_debeEliminar() {
        clienteService.agregarCliente(10, "Para Borrar", "pb@example.com");
        boolean eliminado = clienteService.eliminarCliente(10);
        assertTrue(eliminado);
        assertNull(clienteService.buscarCliente(10));
    }

    @Test
    void eliminarCliente_inexistente_debeFallar() {
        boolean eliminado = clienteService.eliminarCliente(999);
        assertFalse(eliminado);
    }
}
