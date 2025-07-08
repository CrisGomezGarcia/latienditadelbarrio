package Controlador;

import Modelo.DAO.DAO_Marca;
import Modelo.M_ConexionBD;
import Modelo.VO.VO_Marca;
import Vista.V_RegistrarMarca;
import Vista.V_Main;
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

public class C_RegistrarMarca implements InternalFrameListener, ActionListener {

    private Dimension frameSize;
    int locationWidth, locationHeight;
    private V_Main vMain = null;

    private final String titulo = "Catálogos | Marcas | Registrar marca";

    private static V_RegistrarMarca vRegistrarMarca = null;

    public C_RegistrarMarca(V_Main vMain) {
        if (C_RegistrarMarca.vRegistrarMarca == null) {
            C_RegistrarMarca.vRegistrarMarca = new V_RegistrarMarca();
            this.vMain = vMain;
            cargarFormulario();
            cargarEstructuraTabla();
            setListenersParaControlesTablasBotones();
        }
    }

    private void cargarFormulario() {
        vRegistrarMarca.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        frameSize = vRegistrarMarca.getSize();
        locationWidth = ((vMain.desktop.getSize().width - frameSize.width) / 2);
        locationHeight = ((vMain.desktop.getSize().height - frameSize.height) / 2);
        vRegistrarMarca.setLocation(locationWidth, locationHeight);
        vMain.desktop.add(vRegistrarMarca);
        vRegistrarMarca.setTitle(titulo);
        this.setActionsListenerAFormulario();
        vRegistrarMarca.toFront();
        vRegistrarMarca.show();
    }

    private void cargarEstructuraTabla() {
        String[] columnas = {"Nombre", "Descripción"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        vRegistrarMarca.tblMarcas.setModel(tableModel);
        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        tableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        vRegistrarMarca.tblMarcas.getColumnModel().getColumn(0).setMaxWidth(150);
        vRegistrarMarca.tblMarcas.getColumnModel().getColumn(0).setMinWidth(150);
        vRegistrarMarca.tblMarcas.getColumnModel().getColumn(0).setResizable(false);
        vRegistrarMarca.tblMarcas.getColumnModel().getColumn(0).setCellRenderer(tableCellRenderer);

        vRegistrarMarca.tblMarcas.getColumnModel().getColumn(1).setCellRenderer(tableCellRenderer);
    }

    private void setActionsListenerAFormulario() {
        vRegistrarMarca.addInternalFrameListener(this);

        vRegistrarMarca.btnAgregar.setActionCommand("btnAgregar");
        vRegistrarMarca.btnAgregar.addActionListener(this);

        vRegistrarMarca.btnLimpiarCampos.setActionCommand("btnLimpiarCampos");
        vRegistrarMarca.btnLimpiarCampos.addActionListener(this);

        vRegistrarMarca.btnGuardar.setEnabled(false);
        vRegistrarMarca.btnGuardar.setActionCommand("btnGuardar");
        vRegistrarMarca.btnGuardar.addActionListener(this);

        vRegistrarMarca.btnCancelar.setActionCommand("btnCancelar");
        vRegistrarMarca.btnCancelar.addActionListener(this);

        // Configurar que ESC cierre el frame
        vRegistrarMarca.getRootPane().getInputMap(
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW
        ).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                "ESCAPE"
        );

        vRegistrarMarca.getRootPane().getActionMap().put("ESCAPE", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelarPantalla();
            }
        });
        // -> Fin
    }

    private void setListenersParaControlesTablasBotones() {
        // Para el doble click en la tabla
        vRegistrarMarca.tblMarcas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = vRegistrarMarca.tblMarcas.getSelectedRow();
                    int opcion = JOptionPane.showConfirmDialog(vRegistrarMarca,
                            "¿Deseas eliminar esta marca de la tabla?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION);

                    if (opcion == JOptionPane.YES_OPTION) {
                        DefaultTableModel model = (DefaultTableModel) vRegistrarMarca.tblMarcas.getModel();
                        model.removeRow(fila);
                    }
                }

                // 👉 Si ya no quedan filas, deshabilita Guardar
                if (vRegistrarMarca.tblMarcas.getRowCount() == 0) {
                    vRegistrarMarca.btnGuardar.setEnabled(false);
                }
            }
        });

        vRegistrarMarca.txtNombre.addActionListener((ActionEvent e) -> {
            vRegistrarMarca.txtDescripcion.requestFocusInWindow();
        });

        vRegistrarMarca.txtDescripcion.addActionListener((ActionEvent e) -> {
            agregarMarcaATabla();
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String clickName = e.getActionCommand();
        switch (clickName) {
            case "btnAgregar" -> {
                this.agregarMarcaATabla();
            }
            case "btnLimpiarCampos" -> {
                this.limpiarCampos();
            }
            case "btnCancelar" -> {
                this.cancelarPantalla();
            }
            case "btnGuardar" -> {
                this.guardar();
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
        C_RegistrarMarca.vRegistrarMarca = null;
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
    private void agregarMarcaATabla() {
        String nombre = vRegistrarMarca.txtNombre.getText().trim();
        String descripcion = vRegistrarMarca.txtDescripcion.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vRegistrarMarca,
                    "El nombre de la marca es obligatorio.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            vRegistrarMarca.txtNombre.requestFocusInWindow();
            return;
        }

        DefaultTableModel model = (DefaultTableModel) vRegistrarMarca.tblMarcas.getModel();
        model.addRow(new Object[]{nombre, descripcion});

        if (model.getRowCount() > 0) {
            vRegistrarMarca.btnGuardar.setEnabled(true);
        }

        this.limpiarCampos();
    }

    private void limpiarCampos() {
        vRegistrarMarca.txtNombre.setText("");
        vRegistrarMarca.txtDescripcion.setText("");
        vRegistrarMarca.txtNombre.requestFocusInWindow();
    }

    private void cancelarPantalla() {
        int opcion = JOptionPane.showConfirmDialog(vRegistrarMarca,
                "¿Estás seguro de que deseas cancelar y cerrar esta ventana?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            vRegistrarMarca.dispose();
            C_RegistrarMarca.vRegistrarMarca = null;
        }
    }

    private void guardar() {
        JTable tabla = vRegistrarMarca.tblMarcas;

        int totalFilas = tabla.getRowCount();
        List<VO_Marca> listaMarca = new ArrayList<>();
        if (totalFilas == 0) {
            return;
        }

        for (int i = 0; i < totalFilas; i++) {
            String nombre = tabla.getValueAt(i, 0).toString();
            String descripcion = tabla.getValueAt(i, 1).toString();

            VO_Marca vo = new VO_Marca();
            vo.setNombre(nombre);
            vo.setDescripcion(descripcion);

            listaMarca.add(vo);
        }

        try (Connection con = M_ConexionBD.getConexion()) {
            DAO_Marca dao = new DAO_Marca(con);
            boolean exito = dao.guardar(listaMarca);

            if (exito) {
                JOptionPane.showMessageDialog(vRegistrarMarca,
                        "✅ Marcas guardadas correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // 👉 Limpiar tabla y deshabilitar Guardar
                DefaultTableModel model = (DefaultTableModel) tabla.getModel();
                model.setRowCount(0);
                vRegistrarMarca.btnGuardar.setEnabled(false);

            } else {
                JOptionPane.showMessageDialog(vRegistrarMarca,
                        "❌ Ocurrió un error al guardar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vRegistrarMarca,
                    "❌ Error de conexión: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        vRegistrarMarca.txtNombre.requestFocusInWindow();
    }
}
