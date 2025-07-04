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

        //calcular puntos base
        int puntosBase = (int) Math.floor(compra.getMonto() / 100.0);

        //multiplicador según nivel
        double multiplicador = switch (cliente.getNivel()) {
            case BRONCE -> 1.0;
            case PLATA -> 1.2;
            case ORO -> 1.5;
            case PLATINO -> 2.0;
        };

        int puntosCalculados = (int) Math.floor(puntosBase * multiplicador);

        //verificar bonus por 3 compras el mismo día
        List<Compra> comprasCliente = historialCompras.getOrDefault(compra.getIdCliente(), new ArrayList<>());
        long comprasHoy = comprasCliente.stream()
                .filter(c -> c.getFecha().equals(compra.getFecha()))
                .count();

        if (comprasHoy == 2) {
            puntosCalculados += 10;
        }

        //agregar compra al historial en memoria
        comprasCliente.add(compra);
        historialCompras.put(compra.getIdCliente(), comprasCliente);

        //actualizar puntos y nivel
        int nuevosPuntos = cliente.getPuntos() + puntosCalculados;
        NivelFidelidad nuevoNivel = calcularNivelPorPuntos(nuevosPuntos);

        cliente.setPuntos(nuevosPuntos);
        cliente.setNivel(nuevoNivel);

        System.out.println("Compra registrada con éxito. Se otorgaron " + puntosCalculados + " puntos.");
    }

    public NivelFidelidad calcularNivelPorPuntos(int puntos) {
        if (puntos >= 3000) return NivelFidelidad.PLATINO;
        else if (puntos >= 1500) return NivelFidelidad.ORO;
        else if (puntos >= 500) return NivelFidelidad.PLATA;
        else return NivelFidelidad.BRONCE;
    }

    public List<Compra> obtenerHistorialPorCliente(int idCliente) {
        return historialCompras.getOrDefault(idCliente, new ArrayList<>());
    }
}
