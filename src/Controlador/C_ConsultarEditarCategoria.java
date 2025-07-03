/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.DAO_Categoria;
import Modelo.M_ConexionBD;
import Modelo.VO.VO_Categoria;
import Vista.V_ConsultarEditarCategoria;
import Vista.V_JDialog_EditarCategoria;
import Vista.V_Main;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

/**
 *
 * @author Cristian Gomez
 */
public class C_ConsultarEditarCategoria implements ActionListener, InternalFrameListener {

    private Dimension frameSize;
    int locationWidth, locationHeight;
    private V_Main vMain = null;

    private final String titulo = "Catálogos | Categorías | Consultar / Editar categoría";

    private static V_ConsultarEditarCategoria vConsultarEditarCategoria = null;

    private TableRowSorter<DefaultTableModel> sorter;

    public C_ConsultarEditarCategoria(V_Main vMain) {
        if (C_ConsultarEditarCategoria.vConsultarEditarCategoria == null) {
            C_ConsultarEditarCategoria.vConsultarEditarCategoria = new V_ConsultarEditarCategoria();
            this.vMain = vMain;
            cargarFormulario();
            cargarEstructuraTabla();
            setListenersParaControlesTablasBotones();
        }
    }

    private void cargarFormulario() {
        vConsultarEditarCategoria.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        frameSize = vConsultarEditarCategoria.getSize();
        locationWidth = ((vMain.desktop.getSize().width - frameSize.width) / 2);
        locationHeight = ((vMain.desktop.getSize().height - frameSize.height) / 2);
        vConsultarEditarCategoria.setLocation(locationWidth, locationHeight);
        vMain.desktop.add(vConsultarEditarCategoria);
        vConsultarEditarCategoria.setTitle(titulo);
        setActionsListenerAFormulario();
        vConsultarEditarCategoria.toFront();
        vConsultarEditarCategoria.show();
    }

    private void cargarEstructuraTabla() {
        String[] columnas = {"ID", "Nombre", "Descripción", "Estado"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vConsultarEditarCategoria.tblCategorias.setModel(tableModel);

        // Crear y aplicar sorter
        sorter = new TableRowSorter<>(tableModel);
        vConsultarEditarCategoria.tblCategorias.setRowSorter(sorter);

        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        tableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        vConsultarEditarCategoria.tblCategorias.getColumnModel().getColumn(0).setMaxWidth(50);
        vConsultarEditarCategoria.tblCategorias.getColumnModel().getColumn(0).setMinWidth(50);
        vConsultarEditarCategoria.tblCategorias.getColumnModel().getColumn(0).setResizable(false);
        vConsultarEditarCategoria.tblCategorias.getColumnModel().getColumn(0).setCellRenderer(tableCellRenderer);

        vConsultarEditarCategoria.tblCategorias.getColumnModel().getColumn(1).setMaxWidth(150);
        vConsultarEditarCategoria.tblCategorias.getColumnModel().getColumn(1).setMinWidth(150);
        vConsultarEditarCategoria.tblCategorias.getColumnModel().getColumn(1).setResizable(false);
        vConsultarEditarCategoria.tblCategorias.getColumnModel().getColumn(1).setCellRenderer(tableCellRenderer);

        vConsultarEditarCategoria.tblCategorias.getColumnModel().getColumn(2).setCellRenderer(tableCellRenderer);

        vConsultarEditarCategoria.tblCategorias.getColumnModel().getColumn(3).setMaxWidth(80);
        vConsultarEditarCategoria.tblCategorias.getColumnModel().getColumn(3).setMinWidth(80);
        vConsultarEditarCategoria.tblCategorias.getColumnModel().getColumn(3).setResizable(false);
        vConsultarEditarCategoria.tblCategorias.getColumnModel().getColumn(3).setCellRenderer(tableCellRenderer);

        // Configura el buscador después de crear el sorter
        configurarBuscador();

        llenarTabla();
    }

    private void configurarBuscador() {
        vConsultarEditarCategoria.txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
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
                String texto = vConsultarEditarCategoria.txtBuscar.getText();
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

    private void setActionsListenerAFormulario() {
        vConsultarEditarCategoria.addInternalFrameListener(this);

        // Configurar que ESC cierre el frame
        vConsultarEditarCategoria.getRootPane().getInputMap(
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW
        ).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                "ESCAPE"
        );

        vConsultarEditarCategoria.getRootPane().getActionMap().put("ESCAPE", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelarPantalla();
            }
        });
    }

    private void setListenersParaControlesTablasBotones() {
        vConsultarEditarCategoria.tblCategorias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int filaSeleccionada = vConsultarEditarCategoria.tblCategorias.getSelectedRow();
                    if (filaSeleccionada >= 0) {
                        int id = (int) vConsultarEditarCategoria.tblCategorias.getValueAt(filaSeleccionada, 0);
                        String nombre = (String) vConsultarEditarCategoria.tblCategorias.getValueAt(filaSeleccionada, 1);
                        String descripcion = (String) vConsultarEditarCategoria.tblCategorias.getValueAt(filaSeleccionada, 2);
                        String estadoTexto = (String) vConsultarEditarCategoria.tblCategorias.getValueAt(filaSeleccionada, 3);
                        int estado = estadoTexto.equalsIgnoreCase("Activo") ? 1 : 0;

                        VO_Categoria categoriaSeleccionada = new VO_Categoria(id, nombre, descripcion, estado);

                        try (Connection con = M_ConexionBD.getConexion()) {
                            V_JDialog_EditarCategoria dialogo = new V_JDialog_EditarCategoria(vMain, true);
                            new C_JDialog_EditarCategoria(dialogo, categoriaSeleccionada, con);
                            dialogo.setVisible(true);

                            llenarTabla(); // ✅ Recarga después de cerrar el dialog

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(vConsultarEditarCategoria,
                                    "Error obteniendo conexión: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
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
        C_ConsultarEditarCategoria.vConsultarEditarCategoria = null;
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

    private void cancelarPantalla() {
        int opcion = JOptionPane.showConfirmDialog(vConsultarEditarCategoria,
                "¿Estás seguro de que deseas cancelar y cerrar esta ventana?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            vConsultarEditarCategoria.dispose();
            C_ConsultarEditarCategoria.vConsultarEditarCategoria = null;
        }
    }

    private void llenarTabla() {
        try (Connection con = M_ConexionBD.getConexion()) {
            DAO_Categoria dao = new DAO_Categoria(con);
            List<VO_Categoria> listaCategoria = dao.obtenerTodasLasCategorias();

            DefaultTableModel tablaModel = (DefaultTableModel) vConsultarEditarCategoria.tblCategorias.getModel();
            tablaModel.setRowCount(0); // Limpia la tabla

            for (VO_Categoria categoria : listaCategoria) {
                String estadoLegible = categoria.getEstado() == 1 ? "Activo" : "Inactivo";
                tablaModel.addRow(new Object[]{
                    categoria.getId(),
                    categoria.getNombre(),
                    categoria.getDescripcion(),
                    estadoLegible
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error cargando categorias: " + e.getMessage());
        }
    }

}