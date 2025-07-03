/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.DAO_Categoria;
import Modelo.DAO.DAO_Marca;
import Modelo.DAO.DAO_Producto;
import Modelo.M_ConexionBD;
import Modelo.VO.VO_Categoria;
import Modelo.VO.VO_Marca;
import Modelo.VO.VO_Producto;
import Vista.V_Main;
import Vista.V_RegistrarProducto;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.swing.EventComboBoxModel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Cristian Gomez
 */
public class C_RegistrarProducto implements InternalFrameListener, ActionListener {

    private Dimension frameSize;
    int locationWidth, locationHeight;
    private V_Main vMain = null;

    private final String titulo = "Catálogo | Productos | Registrar producto";

    private static V_RegistrarProducto vRegistrarProducto = null;

    public C_RegistrarProducto(V_Main vMain) {
        if (C_RegistrarProducto.vRegistrarProducto == null) {
            C_RegistrarProducto.vRegistrarProducto = new V_RegistrarProducto();
            this.vMain = vMain;
            cargarFormulario();
            cargarEstructuraTabla();
            setListenersParaControlesTablasBotones();
            cargarMarcasCombo();
            cargarCategoriasCombo();
        }
    }

    private void cargarFormulario() {
        vRegistrarProducto.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        frameSize = vRegistrarProducto.getSize();
        locationWidth = ((vMain.desktop.getSize().width - frameSize.width) / 2);
        locationHeight = ((vMain.desktop.getSize().height - frameSize.height) / 2);
        vRegistrarProducto.setLocation(locationWidth, locationHeight);
        vMain.desktop.add(vRegistrarProducto);
        vRegistrarProducto.setTitle(titulo);
        setActionsListenerAFormulario();
        vRegistrarProducto.btnGuardar.setEnabled(false);
        vRegistrarProducto.toFront();
        vRegistrarProducto.show();
    }

    private void cargarEstructuraTabla() {
        String[] columnas = {"Nombre", "ID Marca", "Marca", "Presentación", "ID Categoría", "Categoria", "Código de barras", "Precio Sugerido", "Existencia"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        vRegistrarProducto.tblProductosAgregados.setModel(tableModel);
        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        tableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(0).setMaxWidth(150);
        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(0).setMinWidth(150);
        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(0).setResizable(false);
        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(0).setCellRenderer(tableCellRenderer);

        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(2).setMaxWidth(100);
        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(2).setMinWidth(100);
        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(2).setResizable(false);
        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(2).setCellRenderer(tableCellRenderer);

        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(3).setCellRenderer(tableCellRenderer);

        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(5).setCellRenderer(tableCellRenderer);
        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(6).setCellRenderer(tableCellRenderer);
        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(7).setCellRenderer(tableCellRenderer);

        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(8).setMaxWidth(65);
        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(8).setMinWidth(65);
        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(8).setResizable(false);
        vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(8).setCellRenderer(tableCellRenderer);

        vRegistrarProducto.tblProductosAgregados.getColumnModel().removeColumn(vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(4));
        vRegistrarProducto.tblProductosAgregados.getColumnModel().removeColumn(vRegistrarProducto.tblProductosAgregados.getColumnModel().getColumn(1));
    }

    private void setActionsListenerAFormulario() {
        vRegistrarProducto.addInternalFrameListener(this);

        vRegistrarProducto.txtPrecioSugerido.setText("0");
        vRegistrarProducto.txtExistencia.setText("0");

        vRegistrarProducto.btnAgregarProducto.setActionCommand("agregarProducto");
        vRegistrarProducto.btnAgregarProducto.addActionListener(this);

        vRegistrarProducto.btnLimpiarCampos.setActionCommand("limpiarCampos");
        vRegistrarProducto.btnLimpiarCampos.addActionListener(this);

        vRegistrarProducto.btnGuardar.setActionCommand("btnGuardar");
        vRegistrarProducto.btnGuardar.addActionListener(this);

        vRegistrarProducto.btnCancelar.setActionCommand("btnCancelar");
        vRegistrarProducto.btnCancelar.addActionListener(this);

        // Configurar que ESC cierre el frame
        vRegistrarProducto.getRootPane().getInputMap(
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW
        ).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                "ESCAPE"
        );

        vRegistrarProducto.getRootPane().getActionMap().put("ESCAPE", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelarPantalla();
            }
        });
        // -> Fin
    }

    private void setListenersParaControlesTablasBotones() {
        vRegistrarProducto.txtPrecioSugerido.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();

                // Permitir solo digitos, backspace y punto
                if (!Character.isDigit(c) && c != '.' && c != '\b') {
                    evt.consume();
                }

                if (c == '.' && vRegistrarProducto.txtPrecioSugerido.getText().contains(".")) {
                    evt.consume();
                }
            }
        });

        vRegistrarProducto.txtExistencia.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();

                // Permitir solo digitos, backspace y punto
                if (!Character.isDigit(c) && c != '\b') {
                    evt.consume();
                }
            }
        });

        vRegistrarProducto.tblProductosAgregados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = vRegistrarProducto.tblProductosAgregados.getSelectedRow();
                    int opcion = JOptionPane.showConfirmDialog(vRegistrarProducto,
                            "¿Deseas eliminar esta categoría de la tabla?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION);

                    if (opcion == JOptionPane.YES_OPTION) {
                        DefaultTableModel model = (DefaultTableModel) vRegistrarProducto.tblProductosAgregados.getModel();
                        model.removeRow(fila);
                    }
                }

                // 👉 Si ya no quedan filas, deshabilita Guardar
                if (vRegistrarProducto.tblProductosAgregados.getRowCount() == 0) {
                    vRegistrarProducto.btnGuardar.setEnabled(false);
                }
            }
        });

        vRegistrarProducto.txtNombre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vRegistrarProducto.cboMarcas.requestFocusInWindow();
            }
        });

        vRegistrarProducto.txtPresentacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vRegistrarProducto.cboCategorias.requestFocusInWindow();

            }
        });

        vRegistrarProducto.txtCodigoBarras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vRegistrarProducto.txtPrecioSugerido.requestFocusInWindow();
            }
        });

        vRegistrarProducto.txtPrecioSugerido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vRegistrarProducto.txtExistencia.requestFocusInWindow();
            }
        });

        vRegistrarProducto.txtExistencia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProductoATabla();
            }
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
        C_RegistrarProducto.vRegistrarProducto = null;
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
            case "agregarProducto" -> {
                this.agregarProductoATabla();
            }
            case "limpiarCampos" -> {
                this.limpiarCampos();
            }
            case "btnGuardar" -> {
                this.guardar();
            }
            case "btnCancelar" -> {
                this.cancelarPantalla();
            }
            default ->
                throw new AssertionError();
        }
    }

    // Métodos
    private void cargarCategoriasCombo() {
        try {
            Connection con = M_ConexionBD.getConexion();

            DAO_Categoria dao = new DAO_Categoria(con);
            List<VO_Categoria> listaCategorias = dao.obtenerCategoriasParaCombobox();

            EventList<VO_Categoria> categoriasEventList = new BasicEventList<>();
            categoriasEventList.addAll(listaCategorias);

            EventComboBoxModel<VO_Categoria> model = new EventComboBoxModel<>(categoriasEventList);
            vRegistrarProducto.cboCategorias.setModel(model);

            vRegistrarProducto.cboCategorias.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(
                        JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof VO_Categoria) {
                        setText(((VO_Categoria) value).getNombre());
                    }
                    return this;
                }
            });

            AutoCompleteSupport support = AutoCompleteSupport.install(vRegistrarProducto.cboCategorias, categoriasEventList);
            support.setStrict(true);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error cargando categorías: " + e.getMessage());
        }
    }

    private void cargarMarcasCombo() {
        try {
            Connection con = M_ConexionBD.getConexion();

            DAO_Marca dao = new DAO_Marca(con);
            List<VO_Marca> listaMarcas = dao.obtenerMarcasParaCombobox();

            EventList<VO_Marca> marcasEventList = new BasicEventList<>();
            marcasEventList.addAll(listaMarcas);

            EventComboBoxModel<VO_Marca> model = new EventComboBoxModel<>(marcasEventList);
            vRegistrarProducto.cboMarcas.setModel(model);

            vRegistrarProducto.cboMarcas.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(
                        JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof VO_Marca) {
                        setText(((VO_Marca) value).getNombre());
                    }
                    return this;
                }
            });

            AutoCompleteSupport support = AutoCompleteSupport.install(vRegistrarProducto.cboMarcas, marcasEventList);
            support.setStrict(true);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error cargando marcas: " + e.getMessage());
        }
    }

    private void agregarProductoATabla() {
        String nombre = vRegistrarProducto.txtNombre.getText();
        String presentacion = vRegistrarProducto.txtPresentacion.getText();
        String codigoBarras = vRegistrarProducto.txtCodigoBarras.getText();
        float precioSugerido = Float.parseFloat(vRegistrarProducto.txtPrecioSugerido.getText());
        int existencia = Integer.parseInt(vRegistrarProducto.txtExistencia.getText());

        Object categoriaSeleccionada = vRegistrarProducto.cboCategorias.getSelectedItem();
        Object marcaSeleccionada = vRegistrarProducto.cboMarcas.getSelectedItem();

        if (!(marcaSeleccionada instanceof VO_Marca)) {
            JOptionPane.showMessageDialog(vRegistrarProducto, "Selecciona una marca válida");
            return;
        }
        VO_Marca marca = (VO_Marca) marcaSeleccionada;

        if (!(categoriaSeleccionada instanceof VO_Categoria)) {
            JOptionPane.showMessageDialog(vRegistrarProducto, "Selecciona una categoría válida");
            return;
        }
        VO_Categoria categoria = (VO_Categoria) categoriaSeleccionada;

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vRegistrarProducto,
                    "El nombre del producto es obligatorio.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            vRegistrarProducto.txtNombre.requestFocusInWindow();
            return;
        }

        if (presentacion.isEmpty()) {
            JOptionPane.showMessageDialog(vRegistrarProducto,
                    "La presentación del producto es obligatorio.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            vRegistrarProducto.txtNombre.requestFocusInWindow();
            return;
        }

        if (categoria == null) {
            JOptionPane.showMessageDialog(vRegistrarProducto,
                    "La categoría del producto es obligatorio.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            vRegistrarProducto.txtNombre.requestFocusInWindow();
            return;
        }

        if (codigoBarras.isEmpty()) {
            JOptionPane.showMessageDialog(vRegistrarProducto,
                    "El código de barras del producto es obligatorio.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            vRegistrarProducto.txtNombre.requestFocusInWindow();
            return;
        }

        if (precioSugerido == 0) {
            JOptionPane.showMessageDialog(vRegistrarProducto,
                    "El precio del producto es obligatorio.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            vRegistrarProducto.txtNombre.requestFocusInWindow();
            return;
        }

        DefaultTableModel model = (DefaultTableModel) vRegistrarProducto.tblProductosAgregados.getModel();
        model.addRow(new Object[]{nombre, marca.getId(), marca.getNombre(), presentacion, categoria.getId(), categoria.getNombre(), codigoBarras, precioSugerido, existencia});

        this.limpiarCampos();

        if (model.getRowCount() > 0) {
            vRegistrarProducto.btnGuardar.setEnabled(true);
        }

    }

    private void cancelarPantalla() {
        int opcion = JOptionPane.showConfirmDialog(vRegistrarProducto,
                "¿Estás seguro de que deseas cancelar y cerrar esta ventana?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            vRegistrarProducto.dispose();
            C_RegistrarProducto.vRegistrarProducto = null;
        }
    }

    private void limpiarCampos() {
        vRegistrarProducto.txtNombre.setText("");
        vRegistrarProducto.txtPresentacion.setText("");
        vRegistrarProducto.txtCodigoBarras.setText("");
        vRegistrarProducto.txtPrecioSugerido.setText("0");
        vRegistrarProducto.txtExistencia.setText("0");
        vRegistrarProducto.txtNombre.requestFocusInWindow();

    }

    private void guardar() {
        DefaultTableModel tabla = (DefaultTableModel) vRegistrarProducto.tblProductosAgregados.getModel();

        int totalFilas = tabla.getRowCount();
        List<VO_Producto> listaProducto = new ArrayList<>();
        if (totalFilas == 0) {
            return;
        }

        for (int i = 0; i < totalFilas; i++) {
            String nombre = tabla.getValueAt(i, 0).toString();
            int idMarca = Integer.parseInt(tabla.getValueAt(i, 1).toString());
            String presentacion = tabla.getValueAt(i, 3).toString();
            int idCategoria = Integer.parseInt(tabla.getValueAt(i, 4).toString());
            String codigoBarras = tabla.getValueAt(i, 6).toString();
            double precioSugerido = Double.parseDouble(tabla.getValueAt(i, 7).toString());
            int existencia = Integer.parseInt(tabla.getValueAt(i, 8).toString());

            VO_Producto vo = new VO_Producto();
            vo.setNombre(nombre);
            vo.setIdMarca(idMarca);
            vo.setTipoPresentacion(presentacion);
            vo.setIdCategoria(idCategoria);
            vo.setCodigoBarras(codigoBarras);
            vo.setPrecioSugerido(precioSugerido);
            vo.setStock(existencia);

            listaProducto.add(vo);
        }

        try (Connection con = M_ConexionBD.getConexion()) {
            DAO_Producto dao = new DAO_Producto(con);
            boolean exito = dao.guardar(listaProducto);

            if (exito) {
                JOptionPane.showMessageDialog(vRegistrarProducto,
                        "✅ Productos guardados correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // 👉 Limpiar tabla y deshabilitar Guardar
                tabla.setRowCount(0);
                vRegistrarProducto.btnGuardar.setEnabled(false);
                vRegistrarProducto.txtNombre.requestFocusInWindow();

            } else {
                JOptionPane.showMessageDialog(vRegistrarProducto,
                        "❌ Ocurrió un error al guardar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vRegistrarProducto,
                    "❌ Error de conexión: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
