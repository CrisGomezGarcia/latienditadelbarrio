/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.DAO;

import Modelo.VO.VO_Marca;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristian Gomez
 */
public class DAO_Marca {
    
    private final Connection con;
    
    public DAO_Marca(Connection con) {
        this.con = con;
    }
    
    public boolean guardar(List<VO_Marca> listaMarcas) {
        String sql = "INSERT INTO tbl_marcas(nombre, descripcion) VALUES(?, ?);";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (VO_Marca marca : listaMarcas) {
                ps.setString(1, marca.getNombre());
                if (marca.getDescripcion() == null || marca.getDescripcion().trim().isEmpty()) {
                    ps.setNull(2, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(2, marca.getDescripcion());
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
    
    public List<VO_Marca> obtenerMarcasParaCombobox() throws SQLException {
        List<VO_Marca> listaMarcas = new ArrayList<>();
        String sql = "SELECT id, nombre FROM tbl_marcas WHERE estado = 1;";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                listaMarcas.add(new VO_Marca(id, nombre));
            }
        }
        return listaMarcas;
    }
    
    public List<VO_Marca> obtenerTodasLasMarcas() throws SQLException {
        List<VO_Marca> listaMarcas = new ArrayList<>();
        String sql = "SELECT id, nombre FROM tbl_marcas WHERE estado = 1;";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                listaMarcas.add(new VO_Marca(id, nombre));
            }
        }
        return listaMarcas;
    }
}
