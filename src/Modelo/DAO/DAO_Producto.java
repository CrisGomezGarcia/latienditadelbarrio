package Modelo.DAO;

import Modelo.VO.VO_Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<VO_Producto> obtenerTodosLosProductos() throws SQLException {
        List<VO_Producto> listaProductos = new ArrayList<>();
        String sql = "SELECT * FROM vw_productos_detalle;";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            VO_Producto producto = null;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                producto = new VO_Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setTipoPresentacion(rs.getString("tipo_presentacion"));
                producto.setCodigoBarras(rs.getString("codigo_barras"));
                producto.setPrecioSugerido(rs.getFloat("precio_sugerido"));
                producto.setStock(rs.getInt("stock"));
                producto.setEstado(rs.getInt("estado"));
                producto.setIdCategoria(rs.getInt("categoria_id"));
                producto.setNombreCategoria(rs.getString("categoria_nombre"));
                producto.setIdMarca(rs.getInt("marca_id"));
                producto.setNombreMarca(rs.getString("marca_nombre"));
                listaProductos.add(producto);
            }
        }
        return listaProductos;
    }

    public boolean actualizarProducto(VO_Producto producto) {
        String sql = "UPDATE tbl_productos SET nombre = ?, tipo_presentacion = ?, codigo_barras = ?, precio_sugerido = ?, id_categoria = ?, id_marca = ?, estado = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getTipoPresentacion());
            if (producto.getCodigoBarras() == null || producto.getCodigoBarras().trim().isEmpty()) {
                ps.setNull(3, java.sql.Types.VARCHAR);
            } else {
                ps.setString(3, producto.getCodigoBarras());
            }
            ps.setDouble(4, producto.getPrecioSugerido());
            ps.setInt(5, producto.getIdCategoria());
            ps.setInt(6, producto.getIdMarca());
            ps.setInt(7, producto.getEstado());
            ps.setInt(8, producto.getId());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }
}
