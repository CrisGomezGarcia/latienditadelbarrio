package Modelo.VO;

import java.sql.Date;

public class VO_Producto {

    private int id;
    private String nombre;
    private String tipoPresentacion;
    private String codigoBarras;
    private double precioSugerido;
    private int stock;
    private int idCategoria;
    private String nombreCategoria;
    private int idMarca;
    private String nombreMarca;
    private int estado;
    private Date createdAt;

    public VO_Producto() {
    }

    public VO_Producto(int id, String nombre, String tipoPresentacion, String codigoBarras, double precioSugerido, int stock, int idCategoria, int idMarca, int estado, Date createdAt) {
        this.id = id;
        this.nombre = nombre;
        this.tipoPresentacion = tipoPresentacion;
        this.codigoBarras = codigoBarras;
        this.precioSugerido = precioSugerido;
        this.stock = stock;
        this.idCategoria = idCategoria;
        this.idMarca = idMarca;
        this.estado = estado;
        this.createdAt = createdAt;
    }

    // 👉 Constructor simplificado (sin ID y fecha)
    public VO_Producto(String nombre, String tipoPresentacion, String codigoBarras, double precioSugerido, int stock, int idCategoria, int idMarca) {
        this.nombre = nombre;
        this.tipoPresentacion = tipoPresentacion;
        this.codigoBarras = codigoBarras;
        this.precioSugerido = precioSugerido;
        this.stock = stock;
        this.idCategoria = idCategoria;
        this.idMarca = idMarca;
    }

    // Getters y Setters
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

    public String getTipoPresentacion() {
        return tipoPresentacion;
    }

    public void setTipoPresentacion(String tipoPresentacion) {
        this.tipoPresentacion = tipoPresentacion;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public double getPrecioSugerido() {
        return precioSugerido;
    }

    public void setPrecioSugerido(double precioSugerido) {
        this.precioSugerido = precioSugerido;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return nombre + " - " + tipoPresentacion;
    }
}
