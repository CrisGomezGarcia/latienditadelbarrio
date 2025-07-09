package Controlador;

import Modelo.DAO.DAO_Proveedor;
import Modelo.M_ConexionBD;
import Modelo.VO.VO_Proveedor;
import Vista.Componentes.NumeroCellEditor;
import Vista.V_Main;
import Vista.V_RegistrarProveedor;
import java.sql.Connection;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class C_RegistrarProveedor implements ActionListener, InternalFrameListener {

    private Dimension frameSize;
    int locationWidth, locationHeight;
    private V_Main vMain = null;

    private final String titulo = "Catálogos | Proveedores | Registrar proveedor";

    private static V_RegistrarProveedor vRegistrarProveedor = null;

    public C_RegistrarProveedor(V_Main vMain) {
        if (C_RegistrarProveedor.vRegistrarProveedor == null) {
            C_RegistrarProveedor.vRegistrarProveedor = new V_RegistrarProveedor();
            this.vMain = vMain;
            cargarFormulario();
            cargarEstructuraTabla();
            setListenersParaControlesTablasBotones();
        }
    }

    private void cargarFormulario() {
        vRegistrarProveedor.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        frameSize = vRegistrarProveedor.getSize();
        locationWidth = ((vMain.desktop.getSize().width - frameSize.width) / 2);
        locationHeight = ((vMain.desktop.getSize().height - frameSize.height) / 2);
        vRegistrarProveedor.setLocation(locationWidth, locationHeight);
        vMain.desktop.add(vRegistrarProveedor);
        vRegistrarProveedor.setTitle(titulo);
        this.setActionsListenerAFormulario();
        vRegistrarProveedor.toFront();
        vRegistrarProveedor.show();
    }

    private void cargarEstructuraTabla() {
        String[] columnas = {"RFC", "Nombre", "Teléfono", "Correo electrónico", "Dirección"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columnas) {
        };

        vRegistrarProveedor.tblProveedores.setModel(tableModel);
        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        tableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(0).setMaxWidth(150);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(0).setMinWidth(150);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(0).setResizable(false);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(0).setCellRenderer(tableCellRenderer);

        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(1).setMaxWidth(210);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(1).setMinWidth(210);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(1).setResizable(false);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(1).setCellRenderer(tableCellRenderer);
        // Telefono
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(2).setMaxWidth(150);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(2).setMinWidth(150);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(2).setResizable(false);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(2).setCellEditor(new NumeroCellEditor(10));
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(2).setCellRenderer(tableCellRenderer);

        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(3).setMaxWidth(250);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(3).setMinWidth(250);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(3).setResizable(false);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(3).setCellRenderer(tableCellRenderer);

        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(4).setMaxWidth(300);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(4).setMinWidth(300);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(4).setResizable(false);
        vRegistrarProveedor.tblProveedores.getColumnModel().getColumn(4).setCellRenderer(tableCellRenderer);
    }

    private void setListenersParaControlesTablasBotones() {
        // Para el doble click en la tabla
        vRegistrarProveedor.tblProveedores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 👉 Si selecciona una fila, habilita Eliminar
                int filaSeleccionada = vRegistrarProveedor.tblProveedores.getSelectedRow();
                if (filaSeleccionada != -1) {
                    vRegistrarProveedor.btnEliminar.setEnabled(true);
                } else {
                    vRegistrarProveedor.btnEliminar.setEnabled(false);
                }

                // 👉 Si ya no quedan filas, deshabilita Guardar
                if (vRegistrarProveedor.tblProveedores.getRowCount() == 0) {
                    vRegistrarProveedor.btnGuardar.setEnabled(false);
                }
            }
        });

        vRegistrarProveedor.txtNombre.addActionListener((ActionEvent e) -> {
            vRegistrarProveedor.txtRFC.requestFocusInWindow();
        });

        vRegistrarProveedor.txtRFC.addActionListener((ActionEvent e) -> {
            vRegistrarProveedor.txtTelefono.requestFocusInWindow();
        });

        vRegistrarProveedor.txtTelefono.addActionListener((ActionEvent e) -> {
            vRegistrarProveedor.txtCorreo.requestFocusInWindow();
        });

        vRegistrarProveedor.txtCorreo.addActionListener((ActionEvent e) -> {
            vRegistrarProveedor.txtDireccion.requestFocusInWindow();
        });

        vRegistrarProveedor.txtDireccion.addActionListener((ActionEvent e) -> {
            agregarATabla();
        });

        vRegistrarProveedor.txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();

                // Permitir solo digitos, backspace y punto
                if (!Character.isDigit(c) && c != '\b') {
                    evt.consume();
                }
            }
        });
        
        vRegistrarProveedor.btnGuardar.addActionListener(e -> {
            if (vRegistrarProveedor.tblProveedores.isEditing()) {
                vRegistrarProveedor.tblProveedores.getCellEditor().stopCellEditing();
            }
            guardar();
        });
        
        vRegistrarProveedor.btnEliminar.addActionListener(e -> {
            if (vRegistrarProveedor.tblProveedores.isEditing()) {
                vRegistrarProveedor.tblProveedores.getCellEditor().stopCellEditing();
            }
            eliminarRegistroSeleccionadoTabla();
        });

    }

    private void setActionsListenerAFormulario() {

        vRegistrarProveedor.addInternalFrameListener(this);

        vRegistrarProveedor.btnAgregar.setActionCommand("btnAgregar");
        vRegistrarProveedor.btnAgregar.addActionListener(this);

        vRegistrarProveedor.btnLimpiarCampos.setActionCommand("btnLimpiarCampos");
        vRegistrarProveedor.btnLimpiarCampos.addActionListener(this);

        vRegistrarProveedor.btnGuardar.setEnabled(false);
//        vRegistrarProveedor.btnGuardar.setActionCommand("btnGuardar");
//        vRegistrarProveedor.btnGuardar.addActionListener(this);

        vRegistrarProveedor.btnCancelar.setActionCommand("btnCancelar");
        vRegistrarProveedor.btnCancelar.addActionListener(this);

//        vRegistrarProveedor.btnEliminar.setActionCommand("btnEliminar");
//        vRegistrarProveedor.btnEliminar.addActionListener(this);

        // Configurar que ESC cierre el frame
        vRegistrarProveedor.getRootPane().getInputMap(
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW
        ).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                "ESCAPE"
        );

        vRegistrarProveedor.getRootPane().getActionMap().put("ESCAPE", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelarPantalla();
            }
        });
        // -> Fin

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String clickName = e.getActionCommand();
        switch (clickName) {
            case "btnAgregar" -> {
                this.agregarATabla();
            }
            case "btnLimpiarCampos" -> {
                this.limpiarCampos();
            }
            case "btnCancelar" -> {
                this.cancelarPantalla();
            }
//            case "btnGuardar" -> {
//                this.guardar();
//            }
//            case "btnEliminar" -> {
//                this.eliminarRegistroSeleccionadoTabla();
//            }
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
        C_RegistrarProveedor.vRegistrarProveedor = null;
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
        int opcion = JOptionPane.showConfirmDialog(vRegistrarProveedor,
                "¿Estás seguro de que deseas cancelar y cerrar esta ventana?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            vRegistrarProveedor.dispose();
            C_RegistrarProveedor.vRegistrarProveedor = null;
        }
    }

    private void agregarATabla() {
        String nombre = vRegistrarProveedor.txtNombre.getText();
        String rfc = vRegistrarProveedor.txtRFC.getText();
        String telefono = vRegistrarProveedor.txtTelefono.getText();
        String correo = vRegistrarProveedor.txtCorreo.getText();
        String direccion = vRegistrarProveedor.txtDireccion.getText();

        // Validaciones
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vRegistrarProveedor,
                    "El nombre del proveedor es obligatorio.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            vRegistrarProveedor.txtNombre.requestFocusInWindow();
            return;
        }

        DefaultTableModel model = (DefaultTableModel) vRegistrarProveedor.tblProveedores.getModel();
        model.addRow(new Object[]{rfc, nombre, telefono, correo, direccion});

        this.limpiarCampos();

        if (model.getRowCount() > 0) {
            vRegistrarProveedor.btnGuardar.setEnabled(true);
        }

    }

    private void limpiarCampos() {
        vRegistrarProveedor.txtNombre.setText("");
        vRegistrarProveedor.txtRFC.setText("");
        vRegistrarProveedor.txtTelefono.setText("");
        vRegistrarProveedor.txtCorreo.setText("");
        vRegistrarProveedor.txtDireccion.setText("");
        vRegistrarProveedor.txtNombre.requestFocusInWindow();
    }

    private void guardar() {
        JTable tabla = vRegistrarProveedor.tblProveedores;

        int totalFilas = tabla.getRowCount();
        if (totalFilas == 0) {
            return;
        }

        List<VO_Proveedor> listaProveedores = new ArrayList<>();

        for (int i = 0; i < totalFilas; i++) {
            String rfc = tabla.getValueAt(i, 0).toString();
            String nombre = tabla.getValueAt(i, 1).toString();
            String telefono = tabla.getValueAt(i, 2).toString();
            String correo = tabla.getValueAt(i, 3).toString();
            String direccion = tabla.getValueAt(i, 4).toString();

            listaProveedores.add(new VO_Proveedor(nombre, rfc, telefono, correo, direccion));
        }

        try (Connection con = M_ConexionBD.getConexion()) {
            DAO_Proveedor dao = new DAO_Proveedor(con);
            boolean exito = dao.guardar(listaProveedores);

            if (exito) {
                JOptionPane.showMessageDialog(vRegistrarProveedor,
                        "✅ Proveedores guardados correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // 👉 Limpiar tabla y deshabilitar Guardar
                DefaultTableModel model = (DefaultTableModel) tabla.getModel();
                model.setRowCount(0);
                vRegistrarProveedor.btnGuardar.setEnabled(false);
                vRegistrarProveedor.btnEliminar.setEnabled(false);

            } else {
                JOptionPane.showMessageDialog(vRegistrarProveedor,
                        "❌ Ocurrió un error al guardar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vRegistrarProveedor,
                    "❌ Error de conexión: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

    private void eliminarRegistroSeleccionadoTabla() {
        int fila = vRegistrarProveedor.tblProveedores.getSelectedRow();
        int opcion = JOptionPane.showConfirmDialog(vRegistrarProveedor,
                "¿Deseas eliminar esta categoría de la tabla?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) vRegistrarProveedor.tblProveedores.getModel();
            model.removeRow(fila);
            vRegistrarProveedor.btnEliminar.setEnabled(false);
            if (model.getRowCount() == 0) {
                vRegistrarProveedor.btnGuardar.setEnabled(false);
            }
        }
    }

}
