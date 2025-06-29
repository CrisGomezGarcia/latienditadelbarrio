/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.V_Main;
import Vista.V_RegistrarProducto;
import java.awt.Dimension;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 *
 * @author Cristian Gomez
 */
public class C_RegistrarProducto implements InternalFrameListener {

    private Dimension frameSize;
    int locationWidth, locationHeight;
    private V_Main vMain = null;
    
    private final String titulo = "Productos | Registrar Producto";

    private static V_RegistrarProducto vRegistrarProducto = null;

    public C_RegistrarProducto(V_Main vMain) {
        if (C_RegistrarProducto.vRegistrarProducto == null) {
            C_RegistrarProducto.vRegistrarProducto = new V_RegistrarProducto();
            this.vMain = vMain;
            cargarFormulario();
        }

    }

    public void cargarFormulario() {
        frameSize = vRegistrarProducto.getSize();
        locationWidth = ((vMain.desktop.getSize().width - frameSize.width) / 2);
        locationHeight = ((vMain.desktop.getSize().height - frameSize.height) / 2);
        vRegistrarProducto.setLocation(locationWidth, locationHeight);
        vMain.desktop.add(vRegistrarProducto);
        vRegistrarProducto.setTitle(titulo);
        vRegistrarProducto.addInternalFrameListener(this);
        vRegistrarProducto.toFront();
        vRegistrarProducto.show();
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
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
}
