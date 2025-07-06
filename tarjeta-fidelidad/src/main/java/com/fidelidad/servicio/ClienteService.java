package com.fidelidad.servicio;

import com.fidelidad.modelo.Cliente;
import com.fidelidad.modelo.NivelFidelidad;

import java.util.*;

public class ClienteService {

    private final Map<Integer, Cliente> clientes = new HashMap<>();

    public boolean agregarCliente(int id, String nombre, String correo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return false;
        }

        if (correo == null || !correo.matches("^[^@]+@[^@]+\\.[^@]+$")) {
            System.out.println("Correo inválido.");
            return false;
        }

        if (clientes.containsKey(id)) {
            System.out.println("Ya existe un cliente con ese ID.");
            return false;
        }

        Cliente cliente = new Cliente(id, nombre, correo);
        clientes.put(id, cliente);
        System.out.println("Cliente agregado correctamente.");
        return true;
    }

    public Cliente buscarCliente(int id) {
        return clientes.get(id);
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes.values());
    }

    public boolean actualizarCliente(int id, String nuevoNombre, String nuevoCorreo) {
        Cliente cliente = clientes.get(id);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return false;
        }

        cliente.setNombre(nuevoNombre);
        cliente.setCorreo(nuevoCorreo);
        System.out.println("Cliente actualizado.");
        return true;
    }

    public boolean eliminarCliente(int id) {
        if (!clientes.containsKey(id)) {
            System.out.println("Cliente no encontrado.");
            return false;
        }

        clientes.remove(id);
        System.out.println("Cliente eliminado correctamente.");
        return true;
    }
}
