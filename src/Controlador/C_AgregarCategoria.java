package Controlador;

import Modelo.DAO.DAO_Categoria;
import Modelo.M_ConexionBD;
import Modelo.VO.VO_Categoria;
import Vista.V_AgregarCategoria;
import Vista.V_JDialog_EditarCategoria;
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

public class C_AgregarCategoria implements InternalFrameListener, ActionListener {

    private Dimension frameSize;
    int locationWidth, locationHeight;
    private V_Main vMain = null;

    private final String titulo = "Catálogos | Categorías | Agregar categoría";

    private static V_AgregarCategoria vAgregarCategoria = null;

    public C_AgregarCategoria(V_Main vMain) {
        if (C_AgregarCategoria.vAgregarCategoria == null) {
            C_AgregarCategoria.vAgregarCategoria = new V_AgregarCategoria();
            this.vMain = vMain;
            cargarFormulario();
            cargarEstructuraTabla();
            setListenersParaControlesTablasBotones();
        }
    }

    private void cargarFormulario() {
        vAgregarCategoria.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        frameSize = vAgregarCategoria.getSize();
        locationWidth = ((vMain.desktop.getSize().width - frameSize.width) / 2);
        locationHeight = ((vMain.desktop.getSize().height - frameSize.height) / 2);
        vAgregarCategoria.setLocation(locationWidth, locationHeight);
        vMain.desktop.add(vAgregarCategoria);
        vAgregarCategoria.setTitle(titulo);
        this.setActionsListenerAFormulario();
        vAgregarCategoria.toFront();
        vAgregarCategoria.show();
    }

    private void cargarEstructuraTabla() {
        String[] columnas = {"Nombre", "Descripción"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        vAgregarCategoria.tblCategoriasAgregadas.setModel(tableModel);
        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        tableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        vAgregarCategoria.tblCategoriasAgregadas.getColumnModel().getColumn(0).setMaxWidth(150);
        vAgregarCategoria.tblCategoriasAgregadas.getColumnModel().getColumn(0).setMinWidth(150);
        vAgregarCategoria.tblCategoriasAgregadas.getColumnModel().getColumn(0).setResizable(false);
        vAgregarCategoria.tblCategoriasAgregadas.getColumnModel().getColumn(0).setCellRenderer(tableCellRenderer);

        vAgregarCategoria.tblCategoriasAgregadas.getColumnModel().getColumn(1).setCellRenderer(tableCellRenderer);
    }

    private void setActionsListenerAFormulario() {
        vAgregarCategoria.addInternalFrameListener(this);

        vAgregarCategoria.btnAgregarCategoria.setActionCommand("btnAgregarCategoria");
        vAgregarCategoria.btnAgregarCategoria.addActionListener(this);

        vAgregarCategoria.btnLimpiarCampos.setActionCommand("btnLimpiarCampos");
        vAgregarCategoria.btnLimpiarCampos.addActionListener(this);

        vAgregarCategoria.btnGuardar.setEnabled(false);
        vAgregarCategoria.btnGuardar.setActionCommand("btnGuardar");
        vAgregarCategoria.btnGuardar.addActionListener(this);

        vAgregarCategoria.btnCancelar.setActionCommand("btnCancelar");
        vAgregarCategoria.btnCancelar.addActionListener(this);

        vAgregarCategoria.btnEliminar.setActionCommand("btnEliminar");
        vAgregarCategoria.btnEliminar.addActionListener(this);

        // Configurar que ESC cierre el frame
        vAgregarCategoria.getRootPane().getInputMap(
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW
        ).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                "ESCAPE"
        );

        vAgregarCategoria.getRootPane().getActionMap().put("ESCAPE", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelarPantalla();
            }
        });
        // -> Fin
    }

    private void setListenersParaControlesTablasBotones() {
        // Para el doble click en la tabla
        vAgregarCategoria.tblCategoriasAgregadas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 👉 Si selecciona una fila, habilita Eliminar
                int filaSeleccionada = vAgregarCategoria.tblCategoriasAgregadas.getSelectedRow();
                if (filaSeleccionada != -1) {
                    vAgregarCategoria.btnEliminar.setEnabled(true);
                } else {
                    vAgregarCategoria.btnEliminar.setEnabled(false);
                }
                
                if (e.getClickCount() == 2) {
                    if (filaSeleccionada >= 0) {
                        int modelRow = vAgregarCategoria.tblCategoriasAgregadas.convertRowIndexToModel(filaSeleccionada);
                        
                        String nombre = (String) vAgregarCategoria.tblCategoriasAgregadas.getValueAt(filaSeleccionada, 0);
                        String descripcion = (String) vAgregarCategoria.tblCategoriasAgregadas.getValueAt(filaSeleccionada, 0);

                        VO_Categoria categoriaSeleccionada = new VO_Categoria(0, nombre, descripcion);
                        V_JDialog_EditarCategoria dlg = new V_JDialog_EditarCategoria(vMain, true);
                        C_JDialog_EditarCategoria cDlg = new C_JDialog_EditarCategoria(dlg, categoriaSeleccionada, null);
                        dlg.setVisible(true);
                        
                        if (cDlg.estaActualizado()) {
                            VO_Categoria categoriaSeleccionadaEditada = cDlg.getCategoriaEditada();
                            vAgregarCategoria.tblCategoriasAgregadas.setValueAt(categoriaSeleccionadaEditada.getNombre(), modelRow, 0);
                            vAgregarCategoria.tblCategoriasAgregadas.setValueAt(categoriaSeleccionadaEditada.getDescripcion(), modelRow, 1);
                        }
                    }
                }

                // 👉 Si ya no quedan filas, deshabilita Guardar
                if (vAgregarCategoria.tblCategoriasAgregadas.getRowCount() == 0) {
                    vAgregarCategoria.btnGuardar.setEnabled(false);
                }
            }
        });

        vAgregarCategoria.txtNombreCategoria.addActionListener((ActionEvent e) -> {
            vAgregarCategoria.txtDescripcion.requestFocusInWindow();
        });

        vAgregarCategoria.txtDescripcion.addActionListener((ActionEvent e) -> {
            agregarCategoriaATabla();
        });
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
        C_AgregarCategoria.vAgregarCategoria = null;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String clickName = e.getActionCommand();
        switch (clickName) {
            case "btnAgregarCategoria" -> {
                this.agregarCategoriaATabla();
            }
            case "btnLimpiarCampos" -> {
                this.limpiarCampos();
            }
            case "btnCancelar" -> {
                this.cancelarPantalla();
            }
            case "btnGuardar" -> {
                this.guardarCategoria();
            }
            case "btnEliminar" -> {
                this.eliminarRegistroSeleccionadoTabla();
            }
            default ->
                throw new AssertionError();
        }

    }

    private void agregarCategoriaATabla() {
        String nombre = vAgregarCategoria.txtNombreCategoria.getText().trim();
        String descripcion = vAgregarCategoria.txtDescripcion.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vAgregarCategoria,
                    "El nombre de la categoría es obligatorio.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            vAgregarCategoria.txtNombreCategoria.requestFocusInWindow();
            return;
        }

        DefaultTableModel model = (DefaultTableModel) vAgregarCategoria.tblCategoriasAgregadas.getModel();
        model.addRow(new Object[]{nombre, descripcion});

        if (model.getRowCount() > 0) {
            vAgregarCategoria.btnGuardar.setEnabled(true);
        }

        this.limpiarCampos();
    }

    private void limpiarCampos() {
        vAgregarCategoria.txtNombreCategoria.setText("");
        vAgregarCategoria.txtDescripcion.setText("");
        vAgregarCategoria.txtNombreCategoria.requestFocusInWindow();
    }

    private void cancelarPantalla() {
        int opcion = JOptionPane.showConfirmDialog(vAgregarCategoria,
                "¿Estás seguro de que deseas cancelar y cerrar esta ventana?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            vAgregarCategoria.dispose();
            C_AgregarCategoria.vAgregarCategoria = null;
        }
    }

    private void guardarCategoria() {

        JTable tabla = vAgregarCategoria.tblCategoriasAgregadas;

        int totalFilas = tabla.getRowCount();
        List<VO_Categoria> listaCategorias = new ArrayList<>();
        if (totalFilas == 0) {
            return;
        }

        for (int i = 0; i < totalFilas; i++) {
            String nombre = tabla.getValueAt(i, 0).toString();
            String descripcion = tabla.getValueAt(i, 1).toString();

            VO_Categoria vo = new VO_Categoria();
            vo.setNombre(nombre);
            vo.setDescripcion(descripcion);

            listaCategorias.add(vo);
        }

        try (Connection con = M_ConexionBD.getConexion()) {
            DAO_Categoria dao = new DAO_Categoria(con);

            boolean exito = dao.guardar(listaCategorias);

            if (exito) {
                JOptionPane.showMessageDialog(
                        vAgregarCategoria,
                        "✅ Categorías guardadas correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // 👉 Limpiar tabla y deshabilitar Guardar
                DefaultTableModel model = (DefaultTableModel) tabla.getModel();
                model.setRowCount(0);
                vAgregarCategoria.btnGuardar.setEnabled(false);
                vAgregarCategoria.btnEliminar.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(
                        vAgregarCategoria,
                        "❌ Ocurrió un error al guardar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    vAgregarCategoria,
                    "❌ Error de conexión: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        vAgregarCategoria.txtNombreCategoria.requestFocusInWindow();
    }

    private void eliminarRegistroSeleccionadoTabla() {
        int fila = vAgregarCategoria.tblCategoriasAgregadas.getSelectedRow();
        int opcion = JOptionPane.showConfirmDialog(vAgregarCategoria,
                "¿Deseas eliminar esta categoría de la tabla?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) vAgregarCategoria.tblCategoriasAgregadas.getModel();
            model.removeRow(fila);
            vAgregarCategoria.btnEliminar.setEnabled(false);
            if (model.getRowCount() == 0) {
                vAgregarCategoria.btnGuardar.setEnabled(false);
            }
        }
    }
}
