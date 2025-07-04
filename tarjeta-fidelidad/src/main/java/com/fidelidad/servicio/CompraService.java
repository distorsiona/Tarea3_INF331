package com.fidelidad.servicio;

import com.fidelidad.db.Database;
import com.fidelidad.modelo.Cliente;
import com.fidelidad.modelo.Compra;
import com.fidelidad.modelo.NivelFidelidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        System.out.println("❌ Cliente no encontrado.");
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

    // 3. Verificar bonus por 3 compras el mismo día (consultar base de datos)
    String sqlContarCompras = "SELECT COUNT(*) AS total FROM compras WHERE idCliente = ? AND fecha = ?";
    try (Connection conn = Database.connect();
         PreparedStatement psCount = conn.prepareStatement(sqlContarCompras)) {

        psCount.setInt(1, cliente.getId());
        psCount.setString(2, compra.getFecha().toString());

        try (ResultSet rs = psCount.executeQuery()) {
            if (rs.next() && rs.getInt("total") == 2) {
                puntosCalculados += 10;
            }
        }

        // 4. Insertar la compra
        String sqlInsert = "INSERT INTO compras (idCompra, idCliente, monto, fecha) VALUES (?, ?, ?, ?)";
        try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
            psInsert.setInt(1, compra.getIdCompra());
            psInsert.setInt(2, compra.getIdCliente());
            psInsert.setDouble(3, compra.getMonto());
            psInsert.setString(4, compra.getFecha().toString());
            psInsert.executeUpdate();
        }

        // 5. Actualizar puntos y nivel en la tabla clientes
        int nuevosPuntos = cliente.getPuntos() + puntosCalculados;
        NivelFidelidad nuevoNivel = calcularNivelPorPuntos(nuevosPuntos);

        String sqlUpdateCliente = "UPDATE clientes SET puntos = ?, nivel = ? WHERE id = ?";
        try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateCliente)) {
            psUpdate.setInt(1, nuevosPuntos);
            psUpdate.setString(2, nuevoNivel.name());
            psUpdate.setInt(3, cliente.getId());
            psUpdate.executeUpdate();
        }

        System.out.println("Compra registrada con éxito. Se otorgaron " + puntosCalculados + " puntos.");

    } catch (SQLException e) {
        System.out.println("Error al registrar compra: " + e.getMessage());
    }
}

    public NivelFidelidad calcularNivelPorPuntos(int puntos) {
        if (puntos >= 3000) return NivelFidelidad.PLATINO;
        else if (puntos >= 1500) return NivelFidelidad.ORO;
        else if (puntos >= 500) return NivelFidelidad.PLATA;
        else return NivelFidelidad.BRONCE;
    }

    //PARA EL CRUD DE COMPRAS
    public List<Compra> obtenerHistorialPorCliente(int idCliente) {
        return historialCompras.getOrDefault(idCliente, new ArrayList<>());
    }
}
