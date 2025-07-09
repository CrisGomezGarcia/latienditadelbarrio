package Controlador;

import Modelo.DAO.DAO_Proveedor;
import Modelo.VO.VO_Proveedor;
import Vista.V_JDialog_EditarProveedor;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import javax.swing.JOptionPane;

public class C_JDialog_EditarProveedor {

    private final V_JDialog_EditarProveedor dlg;
    private final VO_Proveedor proveedorSeleccionado;
    private final DAO_Proveedor dao;
    private boolean actualizado = false;

    public C_JDialog_EditarProveedor(V_JDialog_EditarProveedor dlg, VO_Proveedor proveedorSeleccionado, Connection con) {
        this.dlg = dlg;
        this.proveedorSeleccionado = proveedorSeleccionado;
        this.dao = new DAO_Proveedor(con);
        listenersGenerales();
        if (this.proveedorSeleccionado.getId() == 0 && con == null) {
            cargarDatosEditarDesdeCrear();
            setListenersDesdeCrear();
        } else {
            cargarDatosEnDialog();
            setListeners();
        }

    }

    private void listenersGenerales() {
        dlg.txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();

                // Permitir solo digitos, backspace y punto
                if (!Character.isDigit(c) && c != '\b') {
                    evt.consume();
                }
            }
        });
    }

    private void cargarDatosEnDialog() {
        // Precargar datos en los campos del dialog
        dlg.txtNombre.setText(proveedorSeleccionado.getNombre());
        dlg.txtRFC.setText(proveedorSeleccionado.getRfc());
        dlg.txtTelefono.setText(proveedorSeleccionado.getTelefono());
        dlg.txtCorreo.setText(proveedorSeleccionado.getCorreo());
        dlg.txtDireccion.setText(proveedorSeleccionado.getDireccion());
        dlg.chkEstado.setSelected(proveedorSeleccionado.getEstado() == 1);

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
        dlg.setTitle("Proveedores | Editar Proveedor");

        dlg.btnGuardar.addActionListener((ActionEvent e) -> {
            guardarCambios();
        });

        dlg.btnCancelar.addActionListener((ActionEvent e) -> {
            dlg.dispose();
        });
    }

    private void guardarCambios() {
        // Validar campos necesarios
        String nombre = dlg.txtNombre.getText().trim();
        String rfc = dlg.txtRFC.getText().trim();
        String telefono = dlg.txtTelefono.getText().trim();
        String correo = dlg.txtCorreo.getText().trim();
        String direccion = dlg.txtDireccion.getText().trim();
        int activo = dlg.chkEstado.isSelected() ? 1 : 0;

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(dlg, "El nombre no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        proveedorSeleccionado.setNombre(nombre);
        proveedorSeleccionado.setRfc(rfc);
        proveedorSeleccionado.setTelefono(telefono);
        proveedorSeleccionado.setCorreo(correo);
        proveedorSeleccionado.setDireccion(direccion);
        proveedorSeleccionado.setEstado(activo);

        // Guardar en BD
        if (dao.actualizar(proveedorSeleccionado)) {
            JOptionPane.showMessageDialog(dlg, "Proveedor actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dlg.dispose(); // Cierra el dialogo
        } else {
            JOptionPane.showMessageDialog(dlg, "Error al actualizar el proveedor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosEditarDesdeCrear() {
        dlg.panelEstado.setVisible(false);
        dlg.txtNombre.setText(proveedorSeleccionado.getNombre());
        dlg.txtRFC.setText(proveedorSeleccionado.getRfc());
        dlg.txtTelefono.setText(proveedorSeleccionado.getTelefono());
        dlg.txtCorreo.setText(proveedorSeleccionado.getCorreo());
        dlg.txtDireccion.setText(proveedorSeleccionado.getDireccion());
    }

    private void setListenersDesdeCrear() {
        dlg.setTitle("Proveedores | Editar Proveedor");
        dlg.btnGuardar.setText("Actualizar");

        dlg.btnGuardar.addActionListener((ActionEvent e) -> {
            actualizarDesdeCrear();
        });

        dlg.btnCancelar.addActionListener((ActionEvent e) -> {
            dlg.dispose();
        });
    }

    private void actualizarDesdeCrear() {
        proveedorSeleccionado.setRfc(dlg.txtRFC.getText());
        proveedorSeleccionado.setNombre(dlg.txtNombre.getText());
        proveedorSeleccionado.setTelefono(dlg.txtTelefono.getText());
        proveedorSeleccionado.setCorreo(dlg.txtCorreo.getText());
        proveedorSeleccionado.setDireccion(dlg.txtDireccion.getText());
        actualizado = true;
        dlg.dispose();
    }

    public boolean estaActualizado() {
        return actualizado;
    }

    public VO_Proveedor getProveedorEditado() {
        return proveedorSeleccionado;
    }

}
