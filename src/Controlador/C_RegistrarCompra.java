package Controlador;

import Modelo.DAO.DAO_Proveedor;
import Modelo.M_ConexionBD;
import Modelo.VO.VO_Proveedor;
import Vista.V_Main;
import Vista.V_RegistrarCompra;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.swing.EventComboBoxModel;
import java.awt.Color;
import java.sql.Connection;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class C_RegistrarCompra implements ActionListener, InternalFrameListener {

    private Dimension frameSize;
    int locationWidth, locationHeight;
    private V_Main vMain = null;

    private final String titulo = "Compras | Registrar compra";

    private static V_RegistrarCompra vRegistrarCompra = null;

    public C_RegistrarCompra(V_Main vMain) {
        if (C_RegistrarCompra.vRegistrarCompra == null) {
            C_RegistrarCompra.vRegistrarCompra = new V_RegistrarCompra();
            this.vMain = vMain;
            cargarFormulario();
            cargarEstructuraTabla();
            setListenersParaControlesTablasBotones();
            cargarProveedoresCombo();
        }
    }

    private void cargarFormulario() {
        vRegistrarCompra.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        frameSize = vRegistrarCompra.getSize();
        locationWidth = ((vMain.desktop.getSize().width - frameSize.width) / 2);
        locationHeight = ((vMain.desktop.getSize().height - frameSize.height) / 2);
        vRegistrarCompra.setLocation(locationWidth, locationHeight);
        vMain.desktop.add(vRegistrarCompra);
        vRegistrarCompra.setTitle(titulo);
        setActionsListenerAFormulario();
        // Establecer fecha actual
        vRegistrarCompra.jdChooserFechaCompra.setDate(new Date());
        // Obtener el JTextField interno
        JTextField dateField = ((JTextField) vRegistrarCompra.jdChooserFechaCompra.getDateEditor().getUiComponent());
        dateField.setEditable(false);
        dateField.setFocusable(false);
        vRegistrarCompra.jdChooserFechaCompra.setDateFormatString("dd/MM/yyyy");
//        vRegistrarCompra.btnGuardar.setEnabled(false);
        vRegistrarCompra.toFront();
        vRegistrarCompra.show();
    }

    private void cargarEstructuraTabla() {
    }

    private void setListenersParaControlesTablasBotones() {
    }

    private void cargarProveedoresCombo() {
        try (Connection con = M_ConexionBD.getConexion()) {
            DAO_Proveedor daoProveedor = new DAO_Proveedor(con);
            List<VO_Proveedor> listaProveedores = daoProveedor.obtenerProveedoresParaCombobox();

            EventList<VO_Proveedor> proveedoresEventList = new BasicEventList<>();
            proveedoresEventList.addAll(listaProveedores);

            EventComboBoxModel<VO_Proveedor> comboModel = new EventComboBoxModel<>(proveedoresEventList);
            vRegistrarCompra.cboProveedores.setModel(comboModel);

            vRegistrarCompra.cboProveedores.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(
                        JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof VO_Proveedor IProveedor) {
                        setText(IProveedor.getNombre());
                    }
                    return this;
                }
            });

            AutoCompleteSupport support = AutoCompleteSupport.install(vRegistrarCompra.cboProveedores, proveedoresEventList);
            support.setStrict(true);

        } catch (SQLException e) {
            System.out.println("Error cargando proveedores: " + e.getMessage());
        }
    }

    private void setActionsListenerAFormulario() {
        vRegistrarCompra.addInternalFrameListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
        int opcion = JOptionPane.showConfirmDialog(vRegistrarCompra,
                "¿Estás seguro de que deseas cancelar y cerrar esta ventana?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            vRegistrarCompra.dispose();
            C_RegistrarCompra.vRegistrarCompra = null;
        }
    }

}
