package Modelo.DAO;

import Modelo.VO.VO_Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO_Proveedor {

    private final Connection con;

    public DAO_Proveedor(Connection con) {
        this.con = con;
    }

    public boolean guardar(List<VO_Proveedor> listaProveedores) {
        String sql = "INSERT INTO tbl_proveedores(nombre, rfc, telefono, correo, direccion) VALUES(?,?,?,?,?);";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (VO_Proveedor proveedor : listaProveedores) {
                ps.setString(1, proveedor.getNombre());
                // Valida el rfc
                if (proveedor.getRfc() == null || proveedor.getRfc().trim().isEmpty()) {
                    ps.setNull(2, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(2, proveedor.getRfc());
                }
                // Valida el telefono
                if (proveedor.getTelefono() == null || proveedor.getTelefono().trim().isEmpty()) {
                    ps.setNull(3, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(3, proveedor.getTelefono());
                }
                // Valida el correo
                if (proveedor.getCorreo() == null || proveedor.getCorreo().trim().isEmpty()) {
                    ps.setNull(4, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(4, proveedor.getCorreo());
                }
                // Valida la direccion
                if (proveedor.getDireccion() == null || proveedor.getDireccion().trim().isEmpty()) {
                    ps.setNull(5, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(5, proveedor.getDireccion());
                }
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error batch: " + e.getMessage());
            return false;
        }
    }

    public List<VO_Proveedor> obtenerProveedoresParaCombobox() throws SQLException {
        List<VO_Proveedor> listaProveedores = new ArrayList<>();
        String sql = "SELECT id, nombre FROM tbl_proveedores;";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                listaProveedores.add(new VO_Proveedor(id, nombre));
            }
        }
        return listaProveedores;
    }

    public List<VO_Proveedor> obtenerTodosLosProveedores() throws SQLException {
        List<VO_Proveedor> listaProveedores = new ArrayList<>();
        String sql = "SELECT id, nombre, rfc, telefono, correo, direccion, estado FROM tbl_proveedores;";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String rfc = rs.getString("rfc");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                String direccion = rs.getString("direccion");
                int estado = rs.getInt("estado");
                listaProveedores.add(new VO_Proveedor(id, nombre, rfc, telefono, correo, direccion, estado));
            }
        }
        return listaProveedores;
    }

    public boolean actualizar(VO_Proveedor proveedor) {
        String sql = "UPDATE tbl_proveedores SET nombre = ?, rfc = ?, telefono = ?, correo = ?, direccion = ?, estado = ? WHERE id = ?;";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getRfc());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getCorreo());
            ps.setString(5, proveedor.getDireccion());
            ps.setInt(6, proveedor.getEstado());
            ps.setInt(7, proveedor.getId());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar proveedor: " + e.getMessage());
            return false;
        }
    }
}
