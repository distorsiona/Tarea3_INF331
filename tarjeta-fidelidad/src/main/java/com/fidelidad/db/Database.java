package com.fidelidad.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:fidelidad.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void inicializar() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            //tabla de clientes
            String sqlClientes = """
                CREATE TABLE IF NOT EXISTS clientes (
                    id INTEGER PRIMARY KEY,
                    nombre TEXT NOT NULL,
                    correo TEXT NOT NULL,
                    puntos INTEGER NOT NULL DEFAULT 0,
                    nivel TEXT NOT NULL DEFAULT 'BRONCE',
                    streakDias INTEGER NOT NULL DEFAULT 0
                );
                """;
            stmt.execute(sqlClientes);

            //tabla de compras
            String sqlCompras = """
                CREATE TABLE IF NOT EXISTS compras (
                    idCompra INTEGER PRIMARY KEY,
                    idCliente INTEGER NOT NULL,
                    monto REAL NOT NULL,
                    fecha TEXT NOT NULL,
                    FOREIGN KEY (idCliente) REFERENCES clientes(id)
                );
                """;
            stmt.execute(sqlCompras);

        } catch (SQLException e) {
            System.out.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}
