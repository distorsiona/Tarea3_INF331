package com.fidelidad;

import com.fidelidad.modelo.Cliente;
import com.fidelidad.modelo.Compra;
import com.fidelidad.servicio.ClienteService;
import com.fidelidad.servicio.CompraService;

import java.time.LocalDate;
import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteService clienteService = new ClienteService();
    private static final CompraService compraService = new CompraService(clienteService);

    public static void main(String[] args) {
        System.out.println("Sistema de Tarjeta de fidelidad...");

        int opcion;
        do {
            mostrarMenu();
            opcion = leerInt("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> menuClientes();
                case 2 -> registrarCompra();
                case 3 -> mostrarPuntosYNivel();
                case 4 -> System.out.println("Adiós!");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 4);
    }

    private static void mostrarMenu() {
        System.out.println("\n--- Menú Principal ---");
        System.out.println("1. Gestión de Clientes");
        System.out.println("2. Registrar Compra");
        System.out.println("3. Ver Puntos y Nivel de un Cliente");
        System.out.println("4. Salir");
    }

    private static void menuClientes() {
        System.out.println("\n--- Gestión de Clientes ---");
        System.out.println("1. Agregar Cliente");
        System.out.println("2. Listar Clientes");
        System.out.println("3. Actualizar Cliente");
        System.out.println("4. Eliminar Cliente");

        int opcion = leerInt("Seleccione una opción: ");
        switch (opcion) {
        case 1 -> {
            System.out.println("\n--- Agregar Cliente ---");

            //pedir el id para asociar al cliente
            final int[] idHolder = new int[1];
            while (true) {
                idHolder[0] = leerInt("ID (0 para cancelar): ");
                if (idHolder[0] == 0) {
                    System.out.println("Registro cancelado.");
                    break;
                }
                final int idFinal = idHolder[0];
                if (clienteService.listarClientes().stream().anyMatch(c -> c.getId() == idFinal)) {
                    System.out.println("Ya existe un cliente con ese ID. Intente con otro o escriba 0 para cancelar.");
                } else {
                    break;
                }
            }
            if (idHolder[0] == 0) break;

            //pedir nombre del usuario
            String nombre = leerTexto("Nombre (0 para cancelar): ");
            if (nombre.equals("0")) {
                System.out.println("Registro cancelado.");
                break;
            }

            //pedir correo con la respectiva validación
            String correo;
            while (true) {
                correo = leerTexto("Correo electrónico (0 para cancelar): ");
                if (correo.equals("0")) {
                    System.out.println("Registro cancelado.");
                    break;
                }
                if (correoValido(correo)) {
                    break;
                } else {
                    System.out.println("Formato de correo inválido. Intente nuevamente o escriba 0 para cancelar.");
                }
            }

            if (correo.equals("0")) break;

            boolean agregado = clienteService.agregarCliente(idHolder[0], nombre, correo);
            if (agregado) {
                System.out.println("Cliente agregado correctamente.");
            } else {
                System.out.println("No se pudo agregar el cliente. Verifique los datos.");
            }
            }


            case 2 -> clienteService.listarClientes().forEach(System.out::println);
            case 3 -> {
                int id = leerInt("ID del cliente a actualizar: ");
                System.out.print("Nuevo nombre: ");
                String nuevoNombre = scanner.nextLine();

                System.out.print("Nuevo: ");
                String nuevoCorreo = scanner.nextLine();
                if (nuevoCorreo.isEmpty() ||  !nuevoCorreo.contains("@")) {
                    System.out.println("Correo inválido. No se actualizará.");
                    return; //salir si el correo es inválido
                }

                System.out.println("Cliente actualizado con éxito.");
                clienteService.actualizarCliente(id, nuevoNombre, nuevoCorreo);
            }
            case 4 -> {
                int id = leerInt("ID del cliente a eliminar: ");
                System.out.println("¿Está seguro de eliminar al cliente con ID " + id + "? (S/N)");
                String confirmacion = scanner.nextLine();
                if (!confirmacion.equalsIgnoreCase("S")) {
                    System.out.println("Eliminación cancelada.");
                    return; //salir si no se confirma
                }
                clienteService.eliminarCliente(id);
            }
            default -> System.out.println("Opción inválida");
        }
    }

    private static void registrarCompra() {
    System.out.println("\n--- Registrar Compra ---");
    int idCompra = leerInt("ID de la compra: ");

    //se debe validar existencia del cliente con opción de reintentar o cancelar
    Cliente cliente = null;
    int idCliente = -1;

    while (cliente == null) {
        idCliente = leerInt("ID del cliente (0 para cancelar): ");

        if (idCliente == 0) {
            System.out.println("Registro cancelado.");
            return; // sale del método registrarCompra
        }

        cliente = clienteService.buscarCliente(idCliente);
        if (cliente == null) {
            System.out.println("Cliente no encontrado. Intente con otro ID o 0 para cancelar.");
    }
}


    double monto = leerDouble("Monto de la compra: ");
    LocalDate fecha = LocalDate.now(); 

    Compra compra = new Compra(idCompra, idCliente, monto, fecha);
    compraService.registrarCompra(compra);
}


    private static void mostrarPuntosYNivel() {
        int id = leerInt("Ingrese ID del cliente: ");
        Cliente cliente = clienteService.buscarCliente(id);
        if (cliente != null) {
            System.out.println("Cliente: " + cliente.getNombre());
            System.out.println("Puntos: " + cliente.getPuntos());
            System.out.println("Nivel: " + cliente.getNivel());
        } else {
            System.out.println("Cliente no encontrado.");
        }
    }

    private static int leerInt(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Ingrese un número.");
            scanner.next();
            System.out.print(mensaje);
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); //limpiar buffer
        return valor;
    }

    private static double leerDouble(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextDouble()) {
            System.out.println("Entrada inválida. Ingrese un número decimal.");
            scanner.next();
            System.out.print(mensaje);
        }
        double valor = scanner.nextDouble();
        scanner.nextLine(); //limpiar buffer
        return valor;
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    //método para validar el formato de correo electrónico
    private static boolean correoValido(String correo) {
        //validación simple: contiene '@' y un punto después del '@'
        if (correo == null) return false;
        int at = correo.indexOf('@');
        int dot = correo.lastIndexOf('.');
        return at > 0 && dot > at;
    }
}
