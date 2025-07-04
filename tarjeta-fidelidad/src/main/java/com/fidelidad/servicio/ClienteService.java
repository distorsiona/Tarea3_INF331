package com.fidelidad.servicio;

import com.fidelidad.db.Database;
import com.fidelidad.modelo.Cliente;
import com.fidelidad.modelo.NivelFidelidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ClienteService {

    private final Map<Integer, Cliente> clientes = new HashMap<>();


        public boolean agregarCliente(int id, String nombre, String correo) {
        // Validaciones iniciales
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return false;
        }
        
        if (correo == null || !correo.matches("^[^@]+@[^@]+\\.[^@]+$")) {
            System.out.println("Correo inválido.");
            return false;
        }

        // Verificar si ya existe en memoria o en la base de datos
        if (clientes.containsKey(id) || existeClienteEnDB(id)) {
            System.out.println("Ya existe un cliente con ese ID.");
            return false;
        }

        // Insertar en la base de datos
        Connection conn = null;
        try {
            conn = Database.connect();
            if (conn == null) {
                System.out.println("No se pudo conectar a la base de datos.");
                return false;
            }
            
            conn.setAutoCommit(false); // Iniciar transacción
            
            String sql = "INSERT INTO clientes (id, nombre, correo) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, nombre.trim());
                pstmt.setString(3, correo.trim());
                pstmt.executeUpdate();
                conn.commit(); // Confirmar transacción
                
                // Agregar al HashMap solo si la inserción en DB fue exitosa
                Cliente cliente = new Cliente(id, nombre, correo);
                clientes.put(id, cliente);
                
                System.out.println("Cliente agregado correctamente.");
                return true;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Revertir en caso de error
            } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.out.println("Error al agregar cliente a la base de datos: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    // Método auxiliar para verificar si el cliente existe en la base de datos
    private boolean existeClienteEnDB(int id) {
        String sql = "SELECT id FROM clientes WHERE id = ?";
        try (Connection conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Retorna true si encuentra un registro
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar cliente en DB: " + e.getMessage());
            return true; // Asumir que existe para evitar duplicados en caso de error
        }
    }

    

    public Cliente buscarClienteEnDB(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (Connection conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo")
                    );
                    return cliente;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente: " + e.getMessage());
        }

        return null;
    }



    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = Database.connect()) {
            if (conn == null) {
                System.out.println("No se pudo conectar a la base de datos.");
                return clientes;
            }
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM clientes");

            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("correo")
                );
                // Si Cliente tiene setters para puntos, nivel y streakDias, agréguelos aquí
                // cliente.setPuntos(rs.getInt("puntos"));
                // cliente.setNivel(rs.getString("nivel"));
                // cliente.setStreakDias(rs.getInt("streakDias"));
                clientes.add(cliente);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }

        return clientes;
    }

    

    //actualizar cliente cn nombre y/o correo
    public boolean actualizarCliente(int id, String nuevoNombre, String nuevoCorreo) {

        //intentar actualizar en la base de datos
        String sql = "UPDATE clientes SET nombre = ?, correo = ? WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevoNombre);
            pstmt.setString(2, nuevoCorreo);
            pstmt.setInt(3, id);
            int filasActualizadas = pstmt.executeUpdate();
            if (filasActualizadas == 0) {
                System.out.println("Cliente no encontrado.");
                return false;
            }
            // Si existe en el HashMap, actualizar también en memoria
            Cliente cliente = clientes.get(id);
            if (cliente != null) {
                cliente.setNombre(nuevoNombre);
                cliente.setCorreo(nuevoCorreo);
            }
            
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    
    public boolean eliminarCliente(int id) {
        if (!clientes.containsKey(id)) {
            System.out.println("Cliente no encontrado.");
            return false;
        }
        clientes.remove(id);
        System.out.println("Cliente eliminado.");
        return true;
    }


    public Cliente buscarCliente(int id) {
        return clientes.get(id);
    }
}
