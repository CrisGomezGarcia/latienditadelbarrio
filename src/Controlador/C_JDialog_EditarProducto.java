/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.DAO_Categoria;
import Modelo.DAO.DAO_Marca;
import Modelo.DAO.DAO_Producto;
import Modelo.M_ConexionBD;
import Modelo.VO.VO_Categoria;
import Modelo.VO.VO_Marca;
import Modelo.VO.VO_Producto;
import Vista.V_JDialog_EditarProducto;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.swing.EventComboBoxModel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Cristian Gomez
 */
public class C_JDialog_EditarProducto {

    private final V_JDialog_EditarProducto dlg;
    private final VO_Producto productoSeleccionado;
    private final Connection con;
    private final DAO_Producto dao;

    public C_JDialog_EditarProducto(V_JDialog_EditarProducto dlg, VO_Producto productoSeleccionado, Connection con) {
        this.dlg = dlg;
        this.productoSeleccionado = productoSeleccionado;
        this.con = con;
        this.dao = new DAO_Producto(con);
        this.cargarMarcasCombo();
        this.cargarCategoriasCombo();
        cargarDatosProductoEnDialog();
        setListeners();
    }

    private void cargarMarcasCombo() {
        try {
            Connection con = M_ConexionBD.getConexion();

            DAO_Marca dao = new DAO_Marca(con);
            List<VO_Marca> listaMarcas = dao.obtenerMarcasParaCombobox();

            EventList<VO_Marca> marcasEventList = new BasicEventList<>();
            marcasEventList.addAll(listaMarcas);

            EventComboBoxModel<VO_Marca> model = new EventComboBoxModel<>(marcasEventList);
            dlg.cboMarcas.setModel(model);

            dlg.cboMarcas.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(
                        JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof VO_Marca) {
                        setText(((VO_Marca) value).getNombre());
                    }
                    return this;
                }
            });

            AutoCompleteSupport support = AutoCompleteSupport.install(dlg.cboMarcas, marcasEventList);
            support.setStrict(true);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error cargando marcas: " + e.getMessage());
        }
    }

    private void cargarCategoriasCombo() {
        try {
            Connection con = M_ConexionBD.getConexion();

            DAO_Categoria dao = new DAO_Categoria(con);
            List<VO_Categoria> listaCategorias = dao.obtenerCategoriasParaCombobox();

            EventList<VO_Categoria> categoriasEventList = new BasicEventList<>();
            categoriasEventList.addAll(listaCategorias);

            EventComboBoxModel<VO_Categoria> model = new EventComboBoxModel<>(categoriasEventList);
            dlg.cboCategorias.setModel(model);

            dlg.cboCategorias.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(
                        JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof VO_Categoria) {
                        setText(((VO_Categoria) value).getNombre());
                    }
                    return this;
                }
            });

            AutoCompleteSupport support = AutoCompleteSupport.install(dlg.cboCategorias, categoriasEventList);
            support.setStrict(true);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error cargando categorías: " + e.getMessage());
        }
    }
    private void cargarDatosProductoEnDialog() {
        dlg.txtCodigoBarras.setText(productoSeleccionado.getCodigoBarras());
        dlg.txtExistencia.setText(String.valueOf(productoSeleccionado.getStock()));
        dlg.txtExistencia.setEditable(false);
        dlg.txtPresentacion.setText(productoSeleccionado.getTipoPresentacion());
        dlg.txtPrecioSugerido.setText(String.valueOf(productoSeleccionado.getPrecioSugerido()));
        dlg.txtNombre.setText(productoSeleccionado.getNombre());
//        dlg.cboMarcas.setMixingCutoutShape(shape);
    }
    
    private void setListeners() {
        
    }
}
