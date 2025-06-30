/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.DAO;

import Modelo.VO.VO_Categorias;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Cristian Gomez
 */
public class DAO_Categorias {

    private final Connection con;

    public DAO_Categorias(Connection con) {
        this.con = con;
    }

    public boolean guardar(List<VO_Categorias> listaCategorias) {
        String sql = "INSERT INTO tbl_categorias(nombre, descripcion) VALUES(?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (VO_Categorias cat : listaCategorias) {
                ps.setString(1, cat.getNombre());
                if (cat.getDescripcion() == null || cat.getDescripcion().trim().isEmpty()) {
                    ps.setNull(2, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(2, cat.getDescripcion());
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
