/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.VO;

/**
 *
 * @author Cristian Gomez
 */
public class VO_Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private String codigoBarras;
    private float precio;
    private VO_Categoria categoria;
    private int existencia;
    
    public VO_Producto() {
        
    }
    
    public VO_Producto(int id, String nombre, String descripcion, String codigoBarras, float precio, VO_Categoria categoria, int existencia) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.codigoBarras = codigoBarras;
        this.precio = precio;
        this.categoria = categoria;
        this.existencia = existencia;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the codigoBarras
     */
    public String getCodigoBarras() {
        return codigoBarras;
    }

    /**
     * @param codigoBarras the codigoBarras to set
     */
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    /**
     * @return the precio
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    /**
     * @return the categoria
     */
    public VO_Categoria getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(VO_Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * @return the existencia
     */
    public int getStock() {
        return existencia;
    }

    /**
     * @param stock the existencia to set
     */
    public void setStock(int stock) {
        this.existencia = stock;
    }
    
}
