/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.V_AgregarCategoria;
import Vista.V_Main;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Cristian Gomez
 */
public class C_AgregarCategoria implements InternalFrameListener, ActionListener {

    private Dimension frameSize;
    int locationWidth, locationHeight;
    private V_Main vMain = null;

    private final String titulo = "Categorías | Agregar Categoría";

    private static V_AgregarCategoria vAgregarProducto = null;

    public C_AgregarCategoria(V_Main vMain) {
        if (C_AgregarCategoria.vAgregarProducto == null) {
            C_AgregarCategoria.vAgregarProducto = new V_AgregarCategoria();
            this.vMain = vMain;
            cargarFormulario();
            cargarEstructuraTabla();
        }
    }

    public void cargarFormulario() {
        frameSize = vAgregarProducto.getSize();
        locationWidth = ((vMain.desktop.getSize().width - frameSize.width) / 2);
        locationHeight = ((vMain.desktop.getSize().height - frameSize.height) / 2);
        vAgregarProducto.setLocation(locationWidth, locationHeight);
        vMain.desktop.add(vAgregarProducto);
        vAgregarProducto.setTitle(titulo);
        this.setActionsListenerAFormulario();
        vAgregarProducto.toFront();
        vAgregarProducto.show();
    }

    private void cargarEstructuraTabla() {
        String[] columnas = {"NOMBRE", "DESCRIPCION"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        vAgregarProducto.tblCategoriasAgregadas.setModel(tableModel);
        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        tableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        vAgregarProducto.tblCategoriasAgregadas.getColumnModel().getColumn(0).setMaxWidth(150);
        vAgregarProducto.tblCategoriasAgregadas.getColumnModel().getColumn(0).setMinWidth(150);
        vAgregarProducto.tblCategoriasAgregadas.getColumnModel().getColumn(0).setResizable(false);
        vAgregarProducto.tblCategoriasAgregadas.getColumnModel().getColumn(0).setCellRenderer(tableCellRenderer);
    }

    private void setActionsListenerAFormulario() {
        vAgregarProducto.addInternalFrameListener(this);

        vAgregarProducto.btnAgregarCategoria.setActionCommand("AgregarCategoria");
        vAgregarProducto.btnAgregarCategoria.addActionListener(this);

        // Para el doble click en la tabla
        vAgregarProducto.tblCategoriasAgregadas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = vAgregarProducto.tblCategoriasAgregadas.getSelectedRow();
                    int opcion = JOptionPane.showConfirmDialog(vAgregarProducto,
                            "¿Deseas eliminar esta categoría de la tabla?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION);

                    if (opcion == JOptionPane.YES_OPTION) {
                        DefaultTableModel model = (DefaultTableModel) vAgregarProducto.tblCategoriasAgregadas.getModel();
                        model.removeRow(fila);
                    }
                }
            }
        });
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        C_AgregarCategoria.vAgregarProducto = null;
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
            case "AgregarCategoria" -> {
                System.out.println("Agregar producto");
                this.agregarCategoriaATabla();
            }
            default ->
                throw new AssertionError();
        }

    }

    private void agregarCategoriaATabla() {
        String nombre = vAgregarProducto.txtNombreCategoria.getText().toString().trim();
        String descripcion = vAgregarProducto.txtDescripcion.getText().toString().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vAgregarProducto,
                    "El nombre de la categoría es obligatorio.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) vAgregarProducto.tblCategoriasAgregadas.getModel();
        model.addRow(new Object[]{nombre, descripcion});
        vAgregarProducto.txtNombreCategoria.setText("");
        vAgregarProducto.txtDescripcion.setText("");
    }

}
