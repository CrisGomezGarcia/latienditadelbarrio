package Controlador;

import Modelo.DAO.DAO_Categoria;
import Modelo.DAO.DAO_Marca;
import Modelo.DAO.DAO_Producto;
import Modelo.M_ConexionBD;
import Modelo.VO.VO_Categoria;
import Modelo.VO.VO_Marca;
import Modelo.VO.VO_Producto;
import Vista.V_JDialog_EditarProducto;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.swing.EventComboBoxModel;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class C_JDialog_EditarProducto {

    private final V_JDialog_EditarProducto dlg;
    private final VO_Producto productoSeleccionado;
    private final DAO_Producto dao;
    private boolean actualizado = false;

    public C_JDialog_EditarProducto(V_JDialog_EditarProducto dlg, VO_Producto productoSeleccionado, Connection con) {
        this.dlg = dlg;
        this.productoSeleccionado = productoSeleccionado;
        this.dao = new DAO_Producto(con);
        this.cargarMarcasCombo();
        this.cargarCategoriasCombo();
        if (productoSeleccionado.getId() == 0) {
            cargarDatosEditarDesdeCrear();
            setListenersDesdeCrear();
        } else {
            cargarDatosProductoEnDialog();
            setListeners();
        }
        listenersGenerales();
    }

    private void cargarMarcasCombo() {
        try (Connection con = M_ConexionBD.getConexion()) {
            DAO_Marca daoMarca = new DAO_Marca(con);
            List<VO_Marca> listaMarcas = daoMarca.obtenerMarcasParaCombobox();

            EventList<VO_Marca> marcasEventList = new BasicEventList<>();
            marcasEventList.addAll(listaMarcas);

            EventComboBoxModel<VO_Marca> model = new EventComboBoxModel<>(marcasEventList);
            dlg.cboMarcas.setModel(model);

            dlg.cboMarcas.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(
                        JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof VO_Marca IMarca) {
                        setText(IMarca.getNombre());
                    }
                    return this;
                }
            });

            AutoCompleteSupport support = AutoCompleteSupport.install(dlg.cboMarcas, marcasEventList);
            support.setStrict(true);

        } catch (SQLException e) {
            System.out.println("Error cargando marcas: " + e.getMessage());
        }
    }

    private void cargarCategoriasCombo() {
        try (Connection con = M_ConexionBD.getConexion()) {
            DAO_Categoria daoCategoria = new DAO_Categoria(con);
            List<VO_Categoria> listaCategorias = daoCategoria.obtenerCategoriasParaCombobox();

            EventList<VO_Categoria> categoriasEventList = new BasicEventList<>();
            categoriasEventList.addAll(listaCategorias);

            EventComboBoxModel<VO_Categoria> model = new EventComboBoxModel<>(categoriasEventList);
            dlg.cboCategorias.setModel(model);

            dlg.cboCategorias.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(
                        JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof VO_Categoria ICategoria) {
                        setText(ICategoria.getNombre());
                    }
                    return this;
                }
            });

            AutoCompleteSupport support = AutoCompleteSupport.install(dlg.cboCategorias, categoriasEventList);
            support.setStrict(true);

        } catch (SQLException e) {
            System.out.println("Error cargando categorías: " + e.getMessage());
        }
    }

    private void cargarDatosProductoEnDialog() {
        dlg.txtCodigoBarras.setText(productoSeleccionado.getCodigoBarras());
        dlg.txtExistencia.setText(String.valueOf(productoSeleccionado.getStock()));
        dlg.txtExistencia.setEditable(false);
        dlg.txtPresentacion.setText(productoSeleccionado.getTipoPresentacion());
        dlg.txtPrecioSugerido.setText(String.valueOf(productoSeleccionado.getPrecioSugerido()));
        dlg.txtNombre.setText(productoSeleccionado.getNombre());
        dlg.chkEstado.setSelected(productoSeleccionado.getEstado() == 1);

        // 👉 Seleccionar la categoría correcta
        ComboBoxModel<VO_Categoria> modeloCat = dlg.cboCategorias.getModel();
        for (int i = 0; i < modeloCat.getSize(); i++) {
            VO_Categoria cat = modeloCat.getElementAt(i);
            if (cat.getId() == productoSeleccionado.getIdCategoria()) {
                dlg.cboCategorias.setSelectedIndex(i);
                break;
            }
        }

        // 👉 Seleccionar la marca correcta
        ComboBoxModel<VO_Marca> modeloMar = dlg.cboMarcas.getModel();
        for (int i = 0; i < modeloMar.getSize(); i++) {
            VO_Marca mar = modeloMar.getElementAt(i);
            if (mar.getId() == productoSeleccionado.getIdMarca()) {
                dlg.cboMarcas.setSelectedIndex(i);
                break;
            }
        }

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
        dlg.setTitle("Productos | Editar producto");

        dlg.btnGuardar.addActionListener((ActionEvent e) -> {
            guardarCambios();
        });
    }

    private void guardarCambios() {

        String nombre = dlg.txtNombre.getText().trim();
        String presentacion = dlg.txtPresentacion.getText();
        String codigoBarras = dlg.txtCodigoBarras.getText();
        double precioSugerido = Double.parseDouble(dlg.txtPrecioSugerido.getText());
        int activo = dlg.chkEstado.isSelected() ? 1 : 0;

        Object marcaSeleccionada = dlg.cboMarcas.getSelectedItem();
        Object categoriaSeleccionada = dlg.cboCategorias.getSelectedItem();

        if (!(marcaSeleccionada instanceof VO_Marca)) {
            JOptionPane.showMessageDialog(dlg, "Selecciona una marca válida");
            return;
        }
        VO_Marca marca = (VO_Marca) marcaSeleccionada;

        if (!(categoriaSeleccionada instanceof VO_Categoria)) {
            JOptionPane.showMessageDialog(dlg, "Selecciona una categoría válida");
            return;
        }
        VO_Categoria categoria = (VO_Categoria) categoriaSeleccionada;

        // Validar campos vacios o inválidos
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(dlg,
                    "El nombre del producto es obligatorio.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            dlg.txtNombre.requestFocusInWindow();
            return;
        }

        if (presentacion.isEmpty()) {
            JOptionPane.showMessageDialog(dlg,
                    "La presentación del producto es obligatorio.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            dlg.txtPresentacion.requestFocusInWindow();
            return;
        }

        if (categoria == null) {
            JOptionPane.showMessageDialog(dlg,
                    "La categoría del producto es obligatoria.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            dlg.cboCategorias.requestFocusInWindow();
            return;
        }

        if (marca == null) {
            JOptionPane.showMessageDialog(dlg,
                    "La marca del producto es obligatoria.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            dlg.cboMarcas.requestFocusInWindow();
            return;
        }

        if (precioSugerido == 0) {
            JOptionPane.showMessageDialog(dlg,
                    "El precio del producto no debe de ser \"0\".",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            dlg.txtPrecioSugerido.requestFocusInWindow();
            return;
        }

        // Llenar el VO para pasar los datos
        productoSeleccionado.setNombre(nombre);
        productoSeleccionado.setIdMarca(marca.getId());
        productoSeleccionado.setIdCategoria(categoria.getId());
        productoSeleccionado.setTipoPresentacion(presentacion);
        productoSeleccionado.setCodigoBarras(codigoBarras);
        productoSeleccionado.setPrecioSugerido(precioSugerido);
        productoSeleccionado.setEstado(activo);

        // Guardar en BD
        if (dao.actualizarProducto(productoSeleccionado)) {
            JOptionPane.showMessageDialog(dlg, "Producto actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dlg.dispose(); // Cierra el dialogo
        } else {
            JOptionPane.showMessageDialog(dlg, "Error al actualizar la marca.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosEditarDesdeCrear() {
        dlg.panelEstado.setVisible(false);

        dlg.txtNombre.setText(productoSeleccionado.getNombre());
        dlg.txtPresentacion.setText(productoSeleccionado.getTipoPresentacion());
        dlg.txtCodigoBarras.setText(productoSeleccionado.getCodigoBarras());
        dlg.txtPrecioSugerido.setText(String.valueOf(productoSeleccionado.getPrecioSugerido()));
        dlg.txtExistencia.setText(String.valueOf(productoSeleccionado.getStock()));

        // 👉 Seleccionar la categoría correcta
        ComboBoxModel<VO_Categoria> modeloCat = dlg.cboCategorias.getModel();
        for (int i = 0; i < modeloCat.getSize(); i++) {
            VO_Categoria cat = modeloCat.getElementAt(i);
            if (cat.getId() == productoSeleccionado.getIdCategoria()) {
                dlg.cboCategorias.setSelectedIndex(i);
                break;
            }
        }

        // 👉 Seleccionar la marca correcta
        ComboBoxModel<VO_Marca> modeloMar = dlg.cboMarcas.getModel();
        for (int i = 0; i < modeloMar.getSize(); i++) {
            VO_Marca mar = modeloMar.getElementAt(i);
            if (mar.getId() == productoSeleccionado.getIdMarca()) {
                dlg.cboMarcas.setSelectedIndex(i);
                break;
            }
        }

    }

    private void setListenersDesdeCrear() {
        dlg.setTitle("Productos | Editar producto");
        dlg.btnGuardar.setText("Actualizar");

        dlg.btnGuardar.addActionListener((ActionEvent e) -> {
            actualizarDesdeCrear();
        });

    }

    private void actualizarDesdeCrear() {
        productoSeleccionado.setNombre(dlg.txtNombre.getText());
        actualizado = true;
    }

    public boolean estaActualizado() {
        return actualizado;
    }

    public VO_Producto getProductoEditado() {
        return this.productoSeleccionado;
    }

    private void listenersGenerales() {
        dlg.btnCancelar.addActionListener((ActionEvent e) -> {
            dlg.dispose();
        });

        dlg.txtPrecioSugerido.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String texto = dlg.txtPrecioSugerido.getText().trim();
                if (texto.isEmpty()) {
                    dlg.txtPrecioSugerido.setText("0");
                }
            }
        });

        // Este hace que cuando el campo pierda el foco siempre se coloque un 0 para eviatar un error al convertir a double o int
        dlg.txtExistencia.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String texto = dlg.txtExistencia.getText().trim();
                if (texto.isEmpty()) {
                    dlg.txtExistencia.setText("0");
                }
            }
        });

        dlg.txtPrecioSugerido.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();

                // Permitir solo digitos, backspace y punto
                if (!Character.isDigit(c) && c != '.' && c != '\b') {
                    evt.consume();
                }

                if (c == '.' && dlg.txtPrecioSugerido.getText().contains(".")) {
                    evt.consume();
                }
            }
        });

        dlg.txtExistencia.addKeyListener(new java.awt.event.KeyAdapter() {
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
}
