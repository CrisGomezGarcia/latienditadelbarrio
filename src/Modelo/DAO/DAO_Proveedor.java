/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.DAO;

import Modelo.VO.VO_Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Cristian Gomez
 */
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

}
