package com.fidelidad.servicio;

import com.fidelidad.modelo.Cliente;
import com.fidelidad.modelo.Compra;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CompraServiceTest {

    private ClienteService clienteService;
    private CompraService compraService;

    @BeforeEach
    void setUp() {
        clienteService = new ClienteService();
        compraService = new CompraService(clienteService);
    }

    @Test
    void registrarCompra_validaSumaPuntos() {
        clienteService.agregarCliente(1, "Ana", "ana@mail.com");

        Compra compra = new Compra(100, 1, 4500, LocalDate.now());
        compraService.registrarCompra(compra);

        Cliente cliente = clienteService.buscarCliente(1);
        assertEquals(45, cliente.getPuntos()); // 4500 / 100 * 1 (nivel Bronce)
    }

    @Test
void registrarCompra_terceraCompraEnUnDia_bonifica10Puntos() {
    clienteService.agregarCliente(1, "Ana", "ana@mail.com");
    LocalDate hoy = LocalDate.now();

    compraService.registrarCompra(new Compra(101, 1, 1000, hoy)); // 10 pts
    compraService.registrarCompra(new Compra(102, 1, 1000, hoy)); // 10 pts
    compraService.registrarCompra(new Compra(103, 1, 1000, hoy)); // 10 pts + 10 bonus

    Cliente cliente = clienteService.buscarCliente(1);
    assertEquals(40, cliente.getPuntos()); // 10 + 10 + 20
}

@Test
void registrarCompra_actualizaNivelFidelidadCorrectamente() {
    clienteService.agregarCliente(1, "Ana", "ana@mail.com");

    // Sumar 50000 pesos = 500 pts = sube a PLATA
    compraService.registrarCompra(new Compra(201, 1, 50000, LocalDate.now()));

    Cliente cliente = clienteService.buscarCliente(1);
    assertEquals("PLATA", cliente.getNivel().name());

    // +100000 pesos = 1000 más → total 1500 → sube a ORO
    compraService.registrarCompra(new Compra(202, 1, 100000, LocalDate.now()));
    assertEquals("ORO", cliente.getNivel().name());

    // +100000 pesos = 1000 más → total 2500 → aún ORO
    compraService.registrarCompra(new Compra(203, 1, 100000, LocalDate.now()));
    assertEquals("ORO", cliente.getNivel().name());

    // +50000 pesos = +500 → total 3000 → sube a PLATINO
    compraService.registrarCompra(new Compra(204, 1, 50000, LocalDate.now()));
    assertEquals("PLATINO", cliente.getNivel().name());
}


}
