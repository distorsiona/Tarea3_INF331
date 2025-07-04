package com.fidelidad.servicio;

import com.fidelidad.modelo.Cliente;
import com.fidelidad.modelo.Compra;
import com.fidelidad.modelo.NivelFidelidad;

import java.time.LocalDate;
import java.util.*;

public class CompraService {

    private final ClienteService clienteService;
    private final Map<Integer, List<Compra>> historialCompras = new HashMap<>();

    public CompraService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void registrarCompra(Compra compra) {
        Cliente cliente = clienteService.buscarCliente(compra.getIdCliente());

        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        // 1. Calcular puntos base
        int puntosBase = (int) Math.floor(compra.getMonto() / 100.0);

        // 2. Multiplicador según nivel
        double multiplicador = switch (cliente.getNivel()) {
            case BRONCE -> 1.0;
            case PLATA  -> 1.2;
            case ORO    -> 1.5;
            case PLATINO-> 2.0;
        };

        int puntosCalculados = (int) Math.floor(puntosBase * multiplicador);

        // 3. Verificar bonus de 3 compras el mismo día
        List<Compra> comprasDelCliente = historialCompras
                .computeIfAbsent(cliente.getId(), k -> new ArrayList<>());

        long comprasMismoDia = comprasDelCliente.stream()
                .filter(c -> c.getFecha().isEqual(compra.getFecha()))
                .count();

        if (comprasMismoDia == 2) { // Esta sería la tercera compra
            puntosCalculados += 10;
        }

        // 4. Actualizar puntos del cliente
        int puntosTotales = cliente.getPuntos() + puntosCalculados;
        cliente.setPuntos(puntosTotales);

        // 5. Recalcular nivel del cliente
        NivelFidelidad nuevoNivel = calcularNivelPorPuntos(puntosTotales);
        cliente.setNivel(nuevoNivel);

        // 6. Guardar compra en historial
        comprasDelCliente.add(compra);
    }

    private NivelFidelidad calcularNivelPorPuntos(int puntos) {
        if (puntos >= 3000) return NivelFidelidad.PLATINO;
        else if (puntos >= 1500) return NivelFidelidad.ORO;
        else if (puntos >= 500) return NivelFidelidad.PLATA;
        else return NivelFidelidad.BRONCE;
    }

    // Este método puede servir luego si haces un CRUD de compras
    public List<Compra> obtenerHistorialPorCliente(int idCliente) {
        return historialCompras.getOrDefault(idCliente, new ArrayList<>());
    }
}
