/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.DAO;

import Modelo.VO.VO_Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Cristian Gomez
 */
public class DAO_Producto {

    private final Connection con;

    public DAO_Producto(Connection con) {
        this.con = con;
    }

    public boolean guardar(List<VO_Producto> listaProductos) {
        String sql = "INSERT INTO tbl_productos(nombre, tipo_presentacion, codigo_barras, precio_sugerido, stock, id_categoria, id_marca) VALUES(?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (VO_Producto producto : listaProductos) {
                ps.setString(1, producto.getNombre());
                ps.setString(2, producto.getTipoPresentacion());
                if (producto.getCodigoBarras() == null || producto.getCodigoBarras().trim().isEmpty()) {
                    ps.setNull(3, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(3, producto.getCodigoBarras());
                }
                ps.setDouble(4, producto.getPrecioSugerido());
                ps.setInt(5, producto.getStock());
                ps.setInt(6, producto.getIdCategoria());
                ps.setInt(7, producto.getIdMarca());
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
