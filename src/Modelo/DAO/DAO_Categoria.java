/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.DAO;

import Modelo.VO.VO_Categoria;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Cristian Gomez
 */
public class DAO_Categoria {

    private final Connection con;

    public DAO_Categoria(Connection con) {
        this.con = con;
    }

    public boolean guardar(List<VO_Categoria> listaCategorias) {
        String sql = "INSERT INTO tbl_categorias(nombre, descripcion) VALUES(?, ?);";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (VO_Categoria cat : listaCategorias) {
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

    public List<VO_Categoria> obtenerCategoriasParaCombobox() throws SQLException {
        List<VO_Categoria> listaCategorias = new ArrayList<>();
        String sql = "SELECT id, nombre FROM tbl_categorias WHERE estado = 1;";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                listaCategorias.add(new VO_Categoria(id, nombre));
            }
        }
        return listaCategorias;
    }

    public List<VO_Categoria> obtenerTodasLasCategorias() throws SQLException {
        List<VO_Categoria> listaCategorias = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, estado FROM tbl_categorias;";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                int estado = rs.getInt("estado");
                listaCategorias.add(new VO_Categoria(id, nombre, descripcion, estado));
            }
        }
        return listaCategorias;
    }
    
    public boolean actualizarCategoria(VO_Categoria categoria) {
        String sql = "UPDATE tbl_categorias SET nombre = ?, descripcion = ?, estado = ?, updated_at = CURRENT_DATE WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setInt(3, categoria.getEstado());
            ps.setInt(4, categoria.getId());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar categoría: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
