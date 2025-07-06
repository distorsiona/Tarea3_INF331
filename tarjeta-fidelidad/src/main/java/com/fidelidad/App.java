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
    int opcion;
    do {
        System.out.println("\n--- Gestión de Clientes ---");
        System.out.println("1. Agregar Cliente");
        System.out.println("2. Listar Clientes");
        System.out.println("3. Actualizar Cliente");
        System.out.println("4. Eliminar Cliente");
        System.out.println("5. Ver Compras de Cliente");
        System.out.println("6. Eliminar Compra de Cliente");
        System.out.println("7. Volver al Menú Principal");

        opcion = leerInt("Seleccione una opción: ");
        switch (opcion) {
            case 1 -> {
                System.out.println("\n--- Agregar Cliente ---");

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

                String nombre = leerTexto("Nombre (0 para cancelar): ");
                if (nombre.equals("0")) {
                    System.out.println("Registro cancelado.");
                    break;
                }

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
                if (!agregado) {
                    System.out.println("No se pudo agregar el cliente. Verifique los datos.");
                }
            }

            case 2 -> clienteService.listarClientes().forEach(System.out::println);

            case 3 -> {
                int id = leerInt("ID del cliente a actualizar: ");
                System.out.print("Nuevo nombre: ");
                String nuevoNombre = scanner.nextLine();

                System.out.print("Nuevo correo: ");
                String nuevoCorreo = scanner.nextLine();
                if (nuevoCorreo.isEmpty() || !nuevoCorreo.contains("@")) {
                    System.out.println("Correo inválido. No se actualizará.");
                    return;
                }

                clienteService.actualizarCliente(id, nuevoNombre, nuevoCorreo);
                System.out.println("Cliente actualizado con éxito.");
            }

            case 4 -> {
                int id = leerInt("ID del cliente a eliminar: ");
                System.out.println("¿Está seguro de eliminar al cliente con ID " + id + "? (S/N)");
                String confirmacion = scanner.nextLine();
                if (!confirmacion.equalsIgnoreCase("S")) {
                    System.out.println("Eliminación cancelada.");
                    return;
                }
                clienteService.eliminarCliente(id);
            }

            case 5 -> {
                int idCliente = leerInt("Ingrese ID del cliente: ");
                var compras = compraService.obtenerHistorialPorCliente(idCliente);
                if (compras.isEmpty()) {
                    System.out.println("Este cliente no tiene compras registradas.");
                } else {
                    System.out.println("--- Historial de Compras ---");
                    compras.forEach(c ->
                            System.out.println("ID: " + c.getIdCompra() + ", Monto: $" + c.getMonto() + ", Fecha: " + c.getFecha()));
                }
            }

            case 6 -> {
                int idCliente = leerInt("Ingrese ID del cliente: ");
                var compras = compraService.obtenerHistorialPorCliente(idCliente);
                if (compras.isEmpty()) {
                    System.out.println("Este cliente no tiene compras registradas.");
                    break;
                }

                compras.forEach(c ->
                        System.out.println("ID: " + c.getIdCompra() + ", Monto: $" + c.getMonto() + ", Fecha: " + c.getFecha()));
                int idCompra = leerInt("Ingrese ID de la compra que desea eliminar: ");
                boolean eliminada = compras.removeIf(c -> c.getIdCompra() == idCompra);
                if (eliminada) {
                    System.out.println("Compra eliminada con éxito.");
                } else {
                    System.out.println("No se encontró una compra con ese ID.");
                }
            }

            case 7 -> System.out.println("Volviendo al menú principal...");

            default -> {
                if (opcion != 7) System.out.println("Opción inválida.");
            }
        }
    } while (opcion != 7);
}

    private static void registrarCompra() {
        System.out.println("\n--- Registrar Compra ---");
        int idCompra;

        while (true) {
            idCompra = leerInt("ID de la compra (0 para cancelar): ");
            if (idCompra == 0) {
                System.out.println("Registro cancelado.");
                return;
            }

            if (compraService.buscarCompraPorId(idCompra) != null) {
                System.out.println("Ya existe una compra con ese ID. Intente con otro.");
            } else {
                break; // ID válido y único, salimos del bucle
            }
        }

        // Validación de cliente existente
        Cliente cliente = null;
        int idCliente = -1;

        while (cliente == null) {
            idCliente = leerInt("ID del cliente (0 para cancelar): ");
            if (idCliente == 0) {
                System.out.println("Registro cancelado.");
                return;
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

        System.out.println("Compra registrada correctamente.");
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
