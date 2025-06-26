package com.fidelidad;

import com.fidelidad.modelo.Cliente;

public class App {

    public static void main(String[] args) {
        System.out.println("Bienvenido al sistema de Tarjeta de Fidelidad Gamificada 🎮");
        System.out.println("1. Gestionar clientes");
        System.out.println("2. Registrar compra");
        System.out.println("3. Mostrar puntos y nivel");
        System.out.println("4. Salir");

        Cliente cliente = new Cliente(1, "Juan Pérez", "juan@example.com");
        System.out.println(cliente);
    }


    public static String obtenerNivelPorPuntos(int puntos) {
        if (puntos >= 3000) return "Platino";
        else if (puntos >= 1500) return "Oro";
        else if (puntos >= 500) return "Plata";
        else return "Bronce";
    }
}
