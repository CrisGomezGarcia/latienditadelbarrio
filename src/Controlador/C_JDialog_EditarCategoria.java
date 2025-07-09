package Controlador;

import Modelo.DAO.DAO_Categoria;
import Modelo.VO.VO_Categoria;
import Vista.V_JDialog_EditarCategoria;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import javax.swing.JOptionPane;

public class C_JDialog_EditarCategoria {

    private final V_JDialog_EditarCategoria dlg;
    private final VO_Categoria categoriaSeleccionada;
    private final DAO_Categoria dao;
    private boolean actualizado = false;

    public C_JDialog_EditarCategoria(V_JDialog_EditarCategoria dlg, VO_Categoria categoriaSeleccionada, Connection con) {
        this.dlg = dlg;
        this.categoriaSeleccionada = categoriaSeleccionada;
        this.dao = new DAO_Categoria(con);
        if (this.categoriaSeleccionada.getId() == 0 && con == null) {
            cargarDatosEditarDesdeCrear();
            setListenersDesdeCrear();
        } else {
            cargarDatosCategoriaEnDialog();
            setListeners();
        }
    }

    private void cargarDatosCategoriaEnDialog() {
        // Aquí precargas datos en los campos del dialog
        dlg.txtNombre.setText(categoriaSeleccionada.getNombre());
        dlg.txtDescripcion.setText(categoriaSeleccionada.getDescripcion());
        dlg.chkEstado.setSelected(categoriaSeleccionada.getEstado() == 1);

        // Establece el texto inicial según el estado actual
        if (dlg.chkEstado.isSelected()) {
            dlg.chkEstado.setText("Activo");
        } else {
            dlg.chkEstado.setText("Inactivo");
        }

        // Listener para cambiar el texto dinámicamente al marcar/desmarcar
        dlg.chkEstado.addItemListener(e -> {
            if (dlg.chkEstado.isSelected()) {
                dlg.chkEstado.setText("Activo");
            } else {
                dlg.chkEstado.setText("Inactivo");
            }
        });
    }

    private void setListeners() {
        dlg.setTitle("Categorías | Editar categoría");

        dlg.txtNombre.addActionListener((ActionEvent e) -> {
            dlg.txtDescripcion.requestFocusInWindow();
        });

        dlg.btnGuardar.addActionListener((ActionEvent e) -> {
            guardarCambios();
        });

        dlg.btnCancelar.addActionListener((ActionEvent e) -> {
            dlg.dispose();
        });
    }

    private void guardarCambios() {
        // Validar campos si es necesario
        String nombre = dlg.txtNombre.getText().trim();
        String descripcion = dlg.txtDescripcion.getText().trim();
        int activo = dlg.chkEstado.isSelected() ? 1 : 0;

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(dlg, "El nombre no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Actualizar objeto
        categoriaSeleccionada.setNombre(nombre);
        categoriaSeleccionada.setDescripcion(descripcion);
        categoriaSeleccionada.setEstado(activo);

        // Guardar en BD
        if (dao.actualizarCategoria(categoriaSeleccionada)) {
            JOptionPane.showMessageDialog(dlg, "Categoría actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dlg.dispose(); // Cierra el dialogo
        } else {
            JOptionPane.showMessageDialog(dlg, "Error al actualizar la categoría.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosEditarDesdeCrear() {
        dlg.panelEstado.setVisible(false);
        dlg.txtNombre.setText(categoriaSeleccionada.getNombre());
        dlg.txtDescripcion.setText(categoriaSeleccionada.getDescripcion());
    }

    private void setListenersDesdeCrear() {
        dlg.setTitle("Categorías | Editar categoría");
        dlg.btnGuardar.setText("Actualizar");

        dlg.txtNombre.addActionListener((ActionEvent e) -> {
            dlg.txtDescripcion.requestFocusInWindow();
        });

        dlg.btnGuardar.addActionListener((ActionEvent e) -> {
            actualizarDesdeCrear();
        });

        dlg.btnCancelar.addActionListener((ActionEvent e) -> {
            dlg.dispose();
        });
    }

    private void actualizarDesdeCrear() {
        categoriaSeleccionada.setNombre(dlg.txtNombre.getText());
        categoriaSeleccionada.setDescripcion(dlg.txtDescripcion.getText());
        actualizado = true;
        dlg.dispose();
    }

    public boolean estaActualizado() {
        return this.actualizado;
    }

    public VO_Categoria getCategoriaEditada() {
        return this.categoriaSeleccionada;
    }

}
