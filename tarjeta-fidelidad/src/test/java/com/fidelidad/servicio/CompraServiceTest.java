package com.fidelidad.servicio;

import com.fidelidad.modelo.Cliente;
import com.fidelidad.modelo.Compra;
import com.fidelidad.modelo.NivelFidelidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompraServiceTest {

    private ClienteService clienteService;
    private CompraService compraService;

    @BeforeEach
    void setUp() {
        clienteService = new ClienteService();
        compraService = new CompraService(clienteService);
    }

    @Test
    void registrarCompra_clienteInexistente_noHaceNada() {
        Compra compra = new Compra(1, 999, 500, LocalDate.now());
        compraService.registrarCompra(compra);
        assertTrue(compraService.obtenerHistorialPorCliente(999).isEmpty());
    }

    @Test
    void registrarCompra_clienteBronce_asignaPuntosCorrectamente() {
        clienteService.agregarCliente(1, "Ana", "ana@example.com");
        Compra compra = new Compra(1, 1, 1000, LocalDate.now()); // 10 puntos

        compraService.registrarCompra(compra);

        Cliente cliente = clienteService.buscarCliente(1);
        assertEquals(10, cliente.getPuntos());
        assertEquals(NivelFidelidad.BRONCE, cliente.getNivel());
    }

    @Test
    void registrarCompra_clientePlata_asignaPuntosConMultiplicador() {
        //agregar cliente normalmente
        clienteService.agregarCliente(2, "Luis", "luis@example.com");

        //obtener cliente y modificar su nivel y puntos manualmente
        Cliente cliente = clienteService.buscarCliente(2);
        cliente.setPuntos(500);  // Ya es PLATA
        cliente.setNivel(NivelFidelidad.PLATA);

        //registrar compra
        Compra compra = new Compra(1, 2, 1000, LocalDate.now());
        compraService.registrarCompra(compra);

        //validar puntos y nivel
        assertEquals(512, cliente.getPuntos());
        assertEquals(NivelFidelidad.PLATA, cliente.getNivel());
    }



    @Test
    void registrarCompra_tresComprasMismoDia_agregaBonus10() {
        clienteService.agregarCliente(3, "Carlos", "carlos@example.com");
        LocalDate hoy = LocalDate.now();

        compraService.registrarCompra(new Compra(1, 3, 200, hoy)); // 2
        compraService.registrarCompra(new Compra(2, 3, 300, hoy)); // 3
        compraService.registrarCompra(new Compra(3, 3, 100, hoy)); // 1 + 10 bonus

        Cliente cliente = clienteService.buscarCliente(3);
        assertEquals(2 + 3 + 11, cliente.getPuntos()); // Total 16
    }

    @Test
    void registrarCompra_actualizaNivelFidelidadCorrectamente() {
        clienteService.agregarCliente(4, "Eva", "eva@example.com");
        compraService.registrarCompra(new Compra(1, 4, 160_000, LocalDate.now())); // 1600 puntos

        Cliente cliente = clienteService.buscarCliente(4);
        assertEquals(1600, cliente.getPuntos());
        assertEquals(NivelFidelidad.ORO, cliente.getNivel());
    }

    @Test
    void obtenerHistorialPorCliente_retornaComprasCorrectamente() {
        clienteService.agregarCliente(5, "Nico", "nico@example.com");
        LocalDate hoy = LocalDate.now();

        Compra c1 = new Compra(1, 5, 500, hoy);
        Compra c2 = new Compra(2, 5, 1000, hoy);

        compraService.registrarCompra(c1);
        compraService.registrarCompra(c2);

        List<Compra> historial = compraService.obtenerHistorialPorCliente(5);
        assertEquals(2, historial.size());
        assertEquals(c1.getMonto(), historial.get(0).getMonto());
    }
}
