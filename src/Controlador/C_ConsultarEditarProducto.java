/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.DAO_Producto;
import Modelo.M_ConexionBD;
import Modelo.VO.VO_Producto;
import Vista.V_ConsultarEditarProducto;
import Vista.V_Main;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Cristian Gomez
 */
public class C_ConsultarEditarProducto implements ActionListener, InternalFrameListener {

    private Dimension frameSize;
    int locationWidth, locationHeight;
    private V_Main vMain = null;

    private final String titulo = "Catálogos | Productos | Consultar / Editar producto";

    private static V_ConsultarEditarProducto vConsultarEditarProducto = null;

    private TableRowSorter<DefaultTableModel> sorter;

    public C_ConsultarEditarProducto(V_Main vMain) {
        if (C_ConsultarEditarProducto.vConsultarEditarProducto == null) {
            C_ConsultarEditarProducto.vConsultarEditarProducto = new V_ConsultarEditarProducto();
            this.vMain = vMain;
            cargarFormulario();
            cargarEstructuraTabla();
            setListenersParaControlesTablasBotones();
        }
    }

    private void cargarFormulario() {
        vConsultarEditarProducto.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        frameSize = vConsultarEditarProducto.getSize();
        locationWidth = ((vMain.desktop.getSize().width - frameSize.width) / 2);
        locationHeight = ((vMain.desktop.getSize().height - frameSize.height) / 2);
        vConsultarEditarProducto.setLocation(locationWidth, locationHeight);
        vMain.desktop.add(vConsultarEditarProducto);
        vConsultarEditarProducto.setTitle(titulo);
        setActionsListenerAFormulario();
        vConsultarEditarProducto.toFront();
        vConsultarEditarProducto.show();
    }

    private void setActionsListenerAFormulario() {
        vConsultarEditarProducto.addInternalFrameListener(this);
        // Configurar que ESC cierre el frame
        vConsultarEditarProducto.getRootPane().getInputMap(
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW
        ).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                "ESCAPE"
        );

        vConsultarEditarProducto.getRootPane().getActionMap().put("ESCAPE", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelarPantalla();
            }
        });
    }

    private void cargarEstructuraTabla() {
        String[] columnas = {
            "ID", // 0
            "Código de barras", // 1
            "Nombre del producto", // 2
            "ID Marca", // 3
            "Marca", // 4
            "Presentación", // 5
            "ID Categoria", // 6
            "Categoría", // 7
            "Precio Sugerido", // 8
            "Existencia actual", // 9
            "Estado" // 10
        };
        DefaultTableModel tableModel = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vConsultarEditarProducto.tblProductos.setModel(tableModel);

        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        tableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(1).setMaxWidth(165);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(1).setMinWidth(165);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(1).setResizable(false);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(1).setCellRenderer(tableCellRenderer);
        
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(2).setMaxWidth(200);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(2).setMinWidth(200);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(2).setResizable(false);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(2).setCellRenderer(tableCellRenderer);
        
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(4).setMaxWidth(120);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(4).setMinWidth(120);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(4).setResizable(false);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(4).setCellRenderer(tableCellRenderer);
        
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(5).setMaxWidth(250);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(5).setMinWidth(250);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(5).setResizable(false);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(5).setCellRenderer(tableCellRenderer);
        
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(7).setMaxWidth(120);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(7).setMinWidth(120);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(7).setResizable(false);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(7).setCellRenderer(tableCellRenderer);
        
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(8).setMaxWidth(100);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(8).setMinWidth(100);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(8).setResizable(false);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(8).setCellRenderer(tableCellRenderer);
        
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(9).setMaxWidth(100);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(9).setMinWidth(100);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(9).setResizable(false);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(9).setCellRenderer(tableCellRenderer);
        
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(10).setMaxWidth(100);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(10).setMinWidth(100);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(10).setResizable(false);
        vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(10).setCellRenderer(tableCellRenderer);

        vConsultarEditarProducto.tblProductos.getColumnModel().removeColumn(vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(6));
        vConsultarEditarProducto.tblProductos.getColumnModel().removeColumn(vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(3));
        vConsultarEditarProducto.tblProductos.getColumnModel().removeColumn(vConsultarEditarProducto.tblProductos.getColumnModel().getColumn(0));

        // Crear y aplicar sorter
        sorter = new TableRowSorter<>(tableModel);
        vConsultarEditarProducto.tblProductos.setRowSorter(sorter);

        // Configura el buscador después de crear el sorter
        configurarBuscador();

        llenarTabla();
    }

    private void setListenersParaControlesTablasBotones() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        cancelarPantalla();
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        C_ConsultarEditarProducto.vConsultarEditarProducto = null;
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
    }

    // Métodos
    private void cancelarPantalla() {
        int opcion = JOptionPane.showConfirmDialog(vConsultarEditarProducto,
                "¿Estás seguro de que deseas cancelar y cerrar esta ventana?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            vConsultarEditarProducto.dispose();
            C_ConsultarEditarProducto.vConsultarEditarProducto = null;
        }
    }

    private void configurarBuscador() {
        vConsultarEditarProducto.txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarTabla();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarTabla();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarTabla();
            }

            private void filtrarTabla() {
                String texto = vConsultarEditarProducto.txtBuscar.getText();
                if (texto.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                    // Para filtrar solo por columna Nombre: 
                    // sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1));
                }
            }
        });
    }

    private void llenarTabla() {
        try (Connection con = M_ConexionBD.getConexion()) {
            DAO_Producto dao = new DAO_Producto(con);
            List<VO_Producto> listaProducto = dao.obtenerTodosLosProductos();

            DefaultTableModel tableModel = (DefaultTableModel) vConsultarEditarProducto.tblProductos.getModel();
            tableModel.setRowCount(0);

            for (VO_Producto producto : listaProducto) {
                String estadoLegible = producto.getEstado() == 1 ? "Activo" : "Inactivo";
                tableModel.addRow(new Object[]{
                    producto.getId(),
                    producto.getCodigoBarras(),
                    producto.getNombre(),
                    producto.getMarca_id(),
                    producto.getMarca_nombre(),
                    producto.getTipoPresentacion(),
                    producto.getCategoria_id(),
                    producto.getCategoria_nombre(),
                    producto.getPrecioSugerido(),
                    producto.getStock(),
                    estadoLegible
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error cargando productos: " + e.getMessage());
        }
    }

}
