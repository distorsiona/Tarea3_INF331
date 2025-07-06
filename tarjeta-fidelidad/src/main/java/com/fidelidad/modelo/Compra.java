package com.fidelidad.modelo;

import java.time.LocalDate;

public class Compra {
    private final int idCompra;
    private final int idCliente;
    private final double monto;
    private final LocalDate fecha;

    public Compra(int idCompra, int idCliente, double monto) {
        this.idCompra = idCompra;
        this.idCliente = idCliente;
        this.monto = monto;
        fecha = LocalDate.now();
    }

        public Compra(int idCompra, int idCliente, double monto, LocalDate fecha) {
        this.idCompra = idCompra;
        this.idCliente = idCliente;
        this.monto = monto;
        this.fecha = fecha;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
