package Controlador;

import Modelo.DAO.DAO_Proveedor;
import Modelo.M_ConexionBD;
import Modelo.VO.VO_Proveedor;
import Vista.V_ConsultarEditarProveedor;
import Vista.V_JDialog_EditarProveedor;
import Vista.V_Main;
import java.awt.Dimension;
import java.sql.Connection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class C_ConsultarEditarProveedor implements ActionListener, InternalFrameListener {

    private Dimension frameSize;
    int locationWidth, locationHeight;
    private V_Main vMain = null;

    private final String titulo = "Catálogos | Proveedores | Consultar / Editar proveedores";

    private static V_ConsultarEditarProveedor vConsultarEditarProveedor = null;

    private TableRowSorter<DefaultTableModel> sorter;

    public C_ConsultarEditarProveedor(V_Main vMain) {
        if (C_ConsultarEditarProveedor.vConsultarEditarProveedor == null) {
            C_ConsultarEditarProveedor.vConsultarEditarProveedor = new V_ConsultarEditarProveedor();
            this.vMain = vMain;
            cargarFormulario();
            cargarEstructuraTabla();
            setListenersParaControlesTablasBotones();
        }
    }

    private void cargarFormulario() {
        vConsultarEditarProveedor.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        frameSize = vConsultarEditarProveedor.getSize();
        locationWidth = ((vMain.desktop.getSize().width - frameSize.width) / 2);
        locationHeight = ((vMain.desktop.getSize().height - frameSize.height) / 2);
        vConsultarEditarProveedor.setLocation(locationWidth, locationHeight);
        vMain.desktop.add(vConsultarEditarProveedor);
        vConsultarEditarProveedor.setTitle(titulo);
        setActionsListenerAFormulario();
        vConsultarEditarProveedor.toFront();
        vConsultarEditarProveedor.show();
    }

    private void cargarEstructuraTabla() {
        String[] columnas = {"ID", "RFC", "Nombre", "Teléfono", "Correo electrónico", "Dirección", "Estado"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vConsultarEditarProveedor.tblProveedores.setModel(tableModel);
        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        tableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(0).setMaxWidth(50);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(0).setMinWidth(50);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(0).setResizable(false);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(0).setCellRenderer(tableCellRenderer);

        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(1).setMaxWidth(150);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(1).setMinWidth(150);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(1).setResizable(false);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(1).setCellRenderer(tableCellRenderer);

        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(2).setMaxWidth(210);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(2).setMinWidth(210);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(2).setResizable(false);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(2).setCellRenderer(tableCellRenderer);

        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(3).setMaxWidth(150);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(3).setMinWidth(150);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(3).setResizable(false);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(3).setCellRenderer(tableCellRenderer);

        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(4).setMaxWidth(250);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(4).setMinWidth(250);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(4).setResizable(false);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(4).setCellRenderer(tableCellRenderer);

        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(5).setMaxWidth(300);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(5).setMinWidth(300);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(5).setResizable(false);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(5).setCellRenderer(tableCellRenderer);

        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(6).setMaxWidth(80);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(6).setMinWidth(80);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(6).setResizable(false);
        vConsultarEditarProveedor.tblProveedores.getColumnModel().getColumn(6).setCellRenderer(tableCellRenderer);

        // Crear y aplicar sorter
        sorter = new TableRowSorter<>(tableModel);
        vConsultarEditarProveedor.tblProveedores.setRowSorter(sorter);

        // Configura el buscador después de crear el sorter
        configurarBuscador();

        llenarTabla();
    }

    private void configurarBuscador() {
        vConsultarEditarProveedor.txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
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
                String texto = vConsultarEditarProveedor.txtBuscar.getText();
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
        vConsultarEditarProveedor.addInternalFrameListener(this);

        // Configurar que ESC cierre el frame
        vConsultarEditarProveedor.getRootPane().getInputMap(
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW
        ).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                "ESCAPE"
        );

        vConsultarEditarProveedor.getRootPane().getActionMap().put("ESCAPE", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelarPantalla();
            }
        });

        vConsultarEditarProveedor.btnLimpiar.setActionCommand("btnLimpiar");
        vConsultarEditarProveedor.btnLimpiar.addActionListener(this);

        vConsultarEditarProveedor.btnActualizar.setActionCommand("btnActualizar");
        vConsultarEditarProveedor.btnActualizar.addActionListener(this);
    }

    private void setListenersParaControlesTablasBotones() {

        vConsultarEditarProveedor.tblProveedores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int filaSeleccionada = vConsultarEditarProveedor.tblProveedores.getSelectedRow();
                    if (filaSeleccionada >= 0) {
                        int id = (int) vConsultarEditarProveedor.tblProveedores.getValueAt(filaSeleccionada, 0);
                        String rfc = (String) vConsultarEditarProveedor.tblProveedores.getValueAt(filaSeleccionada, 1);
                        String nombre = (String) vConsultarEditarProveedor.tblProveedores.getValueAt(filaSeleccionada, 2);
                        String telefono = (String) vConsultarEditarProveedor.tblProveedores.getValueAt(filaSeleccionada, 3);
                        String correo = (String) vConsultarEditarProveedor.tblProveedores.getValueAt(filaSeleccionada, 4);
                        String direccion = (String) vConsultarEditarProveedor.tblProveedores.getValueAt(filaSeleccionada, 5);
                        String estadoTexto = (String) vConsultarEditarProveedor.tblProveedores.getValueAt(filaSeleccionada, 6);
                        int estado = estadoTexto.equalsIgnoreCase("Activo") ? 1 : 0;

                        VO_Proveedor proveedorSeleccionado = new VO_Proveedor(id, nombre, rfc, telefono, correo, direccion, estado);

                        try (Connection con = M_ConexionBD.getConexion()) {
                            V_JDialog_EditarProveedor dialogo = new V_JDialog_EditarProveedor(vMain, true);
                            new C_JDialog_EditarProveedor(dialogo, proveedorSeleccionado, con);
                            dialogo.setVisible(true);

                            llenarTabla(); // ✅ Recarga después de cerrar el dialog

                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(vConsultarEditarProveedor,
                                    "Error obteniendo conexión: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        vConsultarEditarProveedor.txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                toggleClear();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                toggleClear();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                toggleClear();
            }

            private void toggleClear() {
                vConsultarEditarProveedor.btnLimpiar.setVisible(!vConsultarEditarProveedor.txtBuscar.getText().isEmpty());
            }
        });
        vConsultarEditarProveedor.btnLimpiar.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String clickName = e.getActionCommand();
        switch (clickName) {
            case "btnActualizar" -> {
                this.llenarTabla();
            }
            case "btnLimpiar" -> {
                vConsultarEditarProveedor.txtBuscar.setText(null);
            }
            default ->
                throw new AssertionError();
        }
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
        C_ConsultarEditarProveedor.vConsultarEditarProveedor = null;
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
        int opcion = JOptionPane.showConfirmDialog(vConsultarEditarProveedor,
                "¿Estás seguro de que deseas cancelar y cerrar esta ventana?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            vConsultarEditarProveedor.dispose();
            C_ConsultarEditarProveedor.vConsultarEditarProveedor = null;
        }
    }

    private void llenarTabla() {
        try (Connection con = M_ConexionBD.getConexion()) {
            DAO_Proveedor dao = new DAO_Proveedor(con);
            List<VO_Proveedor> listaProveedor = dao.obtenerTodosLosProveedores();

            DefaultTableModel tablaModel = (DefaultTableModel) vConsultarEditarProveedor.tblProveedores.getModel();
            tablaModel.setRowCount(0);

            for (VO_Proveedor proveedor : listaProveedor) {
                String estadoLegible = proveedor.getEstado() == 1 ? "Activo" : "Inactivo";
                tablaModel.addRow(new Object[]{
                    proveedor.getId(),
                    proveedor.getRfc(),
                    proveedor.getNombre(),
                    proveedor.getTelefono(),
                    proveedor.getCorreo(),
                    proveedor.getDireccion(),
                    estadoLegible
                });
            }

        } catch (SQLException e) {
            System.out.println("Error cargando proveedores: " + e.getMessage());
        }
    }

}
