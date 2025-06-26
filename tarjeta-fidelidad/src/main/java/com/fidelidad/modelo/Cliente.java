package com.fidelidad.modelo;

public class Cliente {
    private int id;
    private String nombre;
    private String correo;
    private int puntos;
    private NivelFidelidad nivel;
    private int streakDias;

    public Cliente(int id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.puntos = 0;
        this.nivel = NivelFidelidad.BRONCE;
        this.streakDias = 0;
    }

    //getters y Setters

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public int getPuntos() { return puntos; }
    public NivelFidelidad getNivel() { return nivel; }
    public int getStreakDias() { return streakDias; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setPuntos(int puntos) { this.puntos = puntos; }
    public void setNivel(NivelFidelidad nivel) { this.nivel = nivel; }
    public void setStreakDias(int streakDias) { this.streakDias = streakDias; }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", puntos=" + puntos +
                ", nivel=" + nivel +
                ", streakDias=" + streakDias +
                '}';
    }
}
