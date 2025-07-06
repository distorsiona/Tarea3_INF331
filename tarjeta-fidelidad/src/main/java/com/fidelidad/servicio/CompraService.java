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

    // CREATE
    public void registrarCompra(Compra compra) {
        Cliente cliente = clienteService.buscarCliente(compra.getIdCliente());

        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        int puntosBase = (int) Math.floor(compra.getMonto() / 100.0);

        double multiplicador = switch (cliente.getNivel()) {
            case BRONCE -> 1.0;
            case PLATA -> 1.2;
            case ORO -> 1.5;
            case PLATINO -> 2.0;
        };

        int puntosCalculados = (int) Math.floor(puntosBase * multiplicador);

        List<Compra> comprasCliente = historialCompras.getOrDefault(compra.getIdCliente(), new ArrayList<>());
        long comprasHoy = comprasCliente.stream()
                .filter(c -> c.getFecha().equals(compra.getFecha()))
                .count();

        if (comprasHoy == 2) {
            puntosCalculados += 10;
        }

        comprasCliente.add(compra);
        historialCompras.put(compra.getIdCliente(), comprasCliente);

        int nuevosPuntos = cliente.getPuntos() + puntosCalculados;
        NivelFidelidad nuevoNivel = calcularNivelPorPuntos(nuevosPuntos);

        cliente.setPuntos(nuevosPuntos);
        cliente.setNivel(nuevoNivel);

        System.out.println("Compra registrada con éxito. Se otorgaron " + puntosCalculados + " puntos.");
    }

    // READ por cliente
    public List<Compra> obtenerHistorialPorCliente(int idCliente) {
        return historialCompras.getOrDefault(idCliente, new ArrayList<>());
    }

    // READ por ID de compra
    public Compra buscarCompraPorId(int idCliente, int idCompra) {
        List<Compra> compras = historialCompras.getOrDefault(idCliente, new ArrayList<>());
        return compras.stream()
                .filter(c -> c.getIdCompra() == idCompra)
                .findFirst()
                .orElse(null);
    }

    // UPDATE: cambia monto o fecha de una compra (por ID)
    public boolean actualizarCompra(int idCliente, int idCompra, double nuevoMonto, LocalDate nuevaFecha) {
        Compra compraExistente = buscarCompraPorId(idCliente, idCompra);
        if (compraExistente == null) return false;

        eliminarCompra(idCliente, idCompra);
        Compra nuevaCompra = new Compra(idCompra, idCliente, nuevoMonto, nuevaFecha);
        registrarCompra(nuevaCompra);
        return true;
    }
// DELETE
public boolean eliminarCompra(int idCliente, int idCompra) {
    List<Compra> compras = historialCompras.get(idCliente);
    if (compras == null) return false;

    boolean eliminado = compras.removeIf(c -> c.getIdCompra() == idCompra);
    if (eliminado) {
        historialCompras.put(idCliente, compras);

        // Recalcular puntos y nivel del cliente
        Cliente cliente = clienteService.buscarCliente(idCliente);
        if (cliente != null) {
            int nuevosPuntos = calcularPuntosTotales(compras, cliente.getNivel());
            cliente.setPuntos(nuevosPuntos);
            NivelFidelidad nuevoNivel = calcularNivelPorPuntos(nuevosPuntos);
            cliente.setNivel(nuevoNivel);
        }

        return true;
    }
    return false;
}

private int calcularPuntosTotales(List<Compra> compras, NivelFidelidad nivel) {
    double multiplicador = switch (nivel) {
        case BRONCE -> 1.0;
        case PLATA -> 1.2;
        case ORO -> 1.5;
        case PLATINO -> 2.0;
    };

    int total = 0;
    for (Compra compra : compras) {
        int puntosBase = (int) Math.floor(compra.getMonto() / 100.0);
        total += (int) Math.floor(puntosBase * multiplicador);
    }
    return total;
}



    // Lógica de niveles
    public NivelFidelidad calcularNivelPorPuntos(int puntos) {
        if (puntos >= 3000) return NivelFidelidad.PLATINO;
        else if (puntos >= 1500) return NivelFidelidad.ORO;
        else if (puntos >= 500) return NivelFidelidad.PLATA;
        else return NivelFidelidad.BRONCE;
    }

    public Compra buscarCompraPorId(int idCompra) {
    for (List<Compra> compras : historialCompras.values()) {
        for (Compra compra : compras) {
            if (compra.getIdCompra() == idCompra) {
                return compra;
            }
        }
    }
    return null;
}

}


