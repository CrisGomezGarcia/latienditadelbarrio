package Modelo.VO;

public class VO_Proveedor {

    private int id;
    private String nombre;
    private String rfc;
    private String telefono;
    private String correo;
    private String direccion;
    private int estado;

    public VO_Proveedor() {

    }

    public VO_Proveedor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    public VO_Proveedor(String nombre, String rfc, String telefono, String correo, String direccion) {
        this.nombre = nombre;
        this.rfc = rfc;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
    }

    public VO_Proveedor(int id, String nombre, String rfc, String telefono, String correo, String direccion, int estado) {
        this.id = id;
        this.nombre = nombre;
        this.rfc = rfc;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.estado = estado;
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

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
