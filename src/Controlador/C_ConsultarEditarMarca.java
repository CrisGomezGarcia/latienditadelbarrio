/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.DAO_Marca;
import Modelo.M_ConexionBD;
import Modelo.VO.VO_Marca;
import Vista.V_ConsultarEditarMarca;
import Vista.V_JDialog_EditarMarca;
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
public class C_ConsultarEditarMarca implements ActionListener, InternalFrameListener {

    private Dimension frameSize;
    int locationWidth, locationHeight;
    private V_Main vMain = null;

    private final String titulo = "Catálogos | Marcas | Consultar / Editar marca";

    private static V_ConsultarEditarMarca vConsultarEditarMarca = null;

    private TableRowSorter<DefaultTableModel> sorter;

    public C_ConsultarEditarMarca(V_Main vMain) {
        if (C_ConsultarEditarMarca.vConsultarEditarMarca == null) {
            C_ConsultarEditarMarca.vConsultarEditarMarca = new V_ConsultarEditarMarca();
            this.vMain = vMain;
            cargarFormulario();
            cargarEstructuraTabla();
            setListenersParaControlesTablasBotones();
        }
    }

    private void cargarFormulario() {
        vConsultarEditarMarca.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        frameSize = vConsultarEditarMarca.getSize();
        locationWidth = ((vMain.desktop.getSize().width - frameSize.width) / 2);
        locationHeight = ((vMain.desktop.getSize().height - frameSize.height) / 2);
        vConsultarEditarMarca.setLocation(locationWidth, locationHeight);
        vMain.desktop.add(vConsultarEditarMarca);
        vConsultarEditarMarca.setTitle(titulo);
        setActionsListenerAFormulario();
        vConsultarEditarMarca.toFront();
        vConsultarEditarMarca.show();
    }

    private void cargarEstructuraTabla() {
        String[] columnas = {"ID", "Nombre", "Descripción", "Estado"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vConsultarEditarMarca.tblMarcas.setModel(tableModel);

        // Crear y aplicar sorter
        sorter = new TableRowSorter<>(tableModel);
        vConsultarEditarMarca.tblMarcas.setRowSorter(sorter);

        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        tableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        vConsultarEditarMarca.tblMarcas.getColumnModel().getColumn(0).setMaxWidth(50);
        vConsultarEditarMarca.tblMarcas.getColumnModel().getColumn(0).setMinWidth(50);
        vConsultarEditarMarca.tblMarcas.getColumnModel().getColumn(0).setResizable(false);
        vConsultarEditarMarca.tblMarcas.getColumnModel().getColumn(0).setCellRenderer(tableCellRenderer);

        vConsultarEditarMarca.tblMarcas.getColumnModel().getColumn(1).setMaxWidth(150);
        vConsultarEditarMarca.tblMarcas.getColumnModel().getColumn(1).setMinWidth(150);
        vConsultarEditarMarca.tblMarcas.getColumnModel().getColumn(1).setResizable(false);
        vConsultarEditarMarca.tblMarcas.getColumnModel().getColumn(1).setCellRenderer(tableCellRenderer);

        vConsultarEditarMarca.tblMarcas.getColumnModel().getColumn(2).setCellRenderer(tableCellRenderer);

        vConsultarEditarMarca.tblMarcas.getColumnModel().getColumn(3).setMaxWidth(80);
        vConsultarEditarMarca.tblMarcas.getColumnModel().getColumn(3).setMinWidth(80);
        vConsultarEditarMarca.tblMarcas.getColumnModel().getColumn(3).setResizable(false);
        vConsultarEditarMarca.tblMarcas.getColumnModel().getColumn(3).setCellRenderer(tableCellRenderer);

        // Configura el buscador después de crear el sorter
        configurarBuscador();

        llenarTabla();
    }

    private void configurarBuscador() {
        vConsultarEditarMarca.txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
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
                String texto = vConsultarEditarMarca.txtBuscar.getText();
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
        vConsultarEditarMarca.addInternalFrameListener(this);

        // Configurar que ESC cierre el frame
        vConsultarEditarMarca.getRootPane().getInputMap(
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW
        ).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                "ESCAPE"
        );

        vConsultarEditarMarca.getRootPane().getActionMap().put("ESCAPE", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelarPantalla();
            }
        });
    }

    private void setListenersParaControlesTablasBotones() {
        vConsultarEditarMarca.tblMarcas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int filaSeleccionada = vConsultarEditarMarca.tblMarcas.getSelectedRow();
                    if (filaSeleccionada >= 0) {
                        int id = (int) vConsultarEditarMarca.tblMarcas.getValueAt(filaSeleccionada, 0);
                        String nombre = (String) vConsultarEditarMarca.tblMarcas.getValueAt(filaSeleccionada, 1);
                        String descripcion = (String) vConsultarEditarMarca.tblMarcas.getValueAt(filaSeleccionada, 2);
                        String estadoTexto = (String) vConsultarEditarMarca.tblMarcas.getValueAt(filaSeleccionada, 3);
                        int estado = estadoTexto.equalsIgnoreCase("Activo") ? 1 : 0;

                        VO_Marca marcaSeleccionada = new VO_Marca(id, nombre, descripcion, estado);

                        try (Connection con = M_ConexionBD.getConexion()) {
                            V_JDialog_EditarMarca dialogo = new V_JDialog_EditarMarca(vMain, true);
                            new C_JDialog_EditarMarca(dialogo, marcaSeleccionada, con);
                            dialogo.setVisible(true);

                            llenarTabla(); // ✅ Recarga después de cerrar el dialog

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(vConsultarEditarMarca,
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
        C_ConsultarEditarMarca.vConsultarEditarMarca = null;
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
        int opcion = JOptionPane.showConfirmDialog(vConsultarEditarMarca,
                "¿Estás seguro de que deseas cancelar y cerrar esta ventana?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            vConsultarEditarMarca.dispose();
            C_ConsultarEditarMarca.vConsultarEditarMarca = null;
        }
    }

    private void llenarTabla() {
        try (Connection con = M_ConexionBD.getConexion()) {
            DAO_Marca dao = new DAO_Marca(con);
            List<VO_Marca> listaMarca = dao.obtenerTodasLasMarcas();

            DefaultTableModel tablaModel = (DefaultTableModel) vConsultarEditarMarca.tblMarcas.getModel();
            tablaModel.setRowCount(0); // Limpia la tabla

            for (VO_Marca marca : listaMarca) {
                String estadoLegible = marca.getEstado() == 1 ? "Activo" : "Inactivo";
                tablaModel.addRow(new Object[]{
                    marca.getId(),
                    marca.getNombre(),
                    marca.getDescripcion(),
                    estadoLegible
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error cargando marcas: " + e.getMessage());
        }
    }

}
