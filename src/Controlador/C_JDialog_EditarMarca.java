/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.DAO_Marca;
import Modelo.VO.VO_Marca;
import Vista.V_JDialog_EditarMarca;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristian Gomez
 */
public class C_JDialog_EditarMarca {

    private final V_JDialog_EditarMarca dlg;
    private final VO_Marca marcaSeleccionada;
    private final DAO_Marca dao;

    public C_JDialog_EditarMarca(V_JDialog_EditarMarca dlg, VO_Marca marcaSeleccionada, Connection con) {
        this.dlg = dlg;
        this.marcaSeleccionada = marcaSeleccionada;
        this.dao = new DAO_Marca(con);
        cargarDatosMarcaEnDialog();
        setListeners();
    }

    private void cargarDatosMarcaEnDialog() {
        // Aquí precargas datos en los campos del dialog
        dlg.txtNombre.setText(marcaSeleccionada.getNombre());
        dlg.txtDescripcion.setText(marcaSeleccionada.getDescripcion());
        dlg.chkEstado.setSelected(marcaSeleccionada.getEstado() == 1);

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
        
        dlg.setTitle("Marcas | Editar marca");
        
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
        marcaSeleccionada.setNombre(nombre);
        marcaSeleccionada.setDescripcion(descripcion);
        marcaSeleccionada.setEstado(activo);

        // Guardar en BD
        if (dao.actualizarMarca(marcaSeleccionada)) {
            JOptionPane.showMessageDialog(dlg, "Marca actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dlg.dispose(); // Cierra el dialogo
        } else {
            JOptionPane.showMessageDialog(dlg, "Error al actualizar la marca.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
