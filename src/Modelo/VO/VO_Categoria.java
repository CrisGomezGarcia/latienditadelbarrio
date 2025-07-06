package Modelo.VO;

import java.time.LocalDate;

/**
 * @author Cristian Gomez
 */
public class VO_Categoria {

    private int id;
    private String nombre;
    private String descripcion;
    private int estado; // o boolean
    private LocalDate createdAt;

    public VO_Categoria() {
    }

    public VO_Categoria(int id, String nombre, String descripcion, int estado, LocalDate createdAt) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.createdAt = createdAt;
    }
    
    public VO_Categoria(int id, String nombre, String descripcion, int estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public VO_Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return nombre;
    }
}