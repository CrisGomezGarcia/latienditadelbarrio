/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.V_Main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

/**
 *
 * @author Cristian Gomez
 */
public class C_Main implements ActionListener {
    
    private V_Main vMain = null;
    private final String title = "Punto de Venta | La Tiendita del barrio";
    
    public C_Main(V_Main vMain) {
        this.vMain = vMain;
    }
    
    public void CargarFormulario() {
        this.vMain.setTitle(title);
        this.vMain.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setActionsListenerAMenus();
        
        this.vMain.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String clickName = e.getActionCommand();
        switch (clickName) {
            case "registrarProducto" -> {
                new C_RegistrarProducto(this.vMain);
            }
            case "consultarEditarProducto" -> {
                new C_ConsultarEditarProducto(this.vMain);
            }
            case "agregarCategoría" -> {
                new C_AgregarCategoria(this.vMain);
            }
            case "consultarEditarCategoria" -> {
                new C_ConsultarEditarCategoria(this.vMain);
            }
            case "registrarMarca" -> {
                new C_RegistrarMarca(this.vMain);
            }
            case "consultarEditarMarca" -> {
                new C_ConsultarEditarMarca(this.vMain);
            }
            case "registrarProveedor" -> {
                new C_RegistrarProveedor(this.vMain);
            }
            default -> throw new AssertionError();
        }
    }
    
    private void setActionsListenerAMenus() {
        this.vMain.menuRegistrarProducto.setActionCommand("registrarProducto");
        this.vMain.menuRegistrarProducto.addActionListener(this);
        
        this.vMain.menuConsultarEditarProducto.setActionCommand("consultarEditarProducto");
        this.vMain.menuConsultarEditarProducto.addActionListener(this);
        
        this.vMain.menuAgregarCategoria.setActionCommand("agregarCategoría");
        this.vMain.menuAgregarCategoria.addActionListener(this);
        
        this.vMain.menuConsultarEditarCategorias.setActionCommand("consultarEditarCategoria");
        this.vMain.menuConsultarEditarCategorias.addActionListener(this);
        
        this.vMain.menuRegistrarMarca.setActionCommand("registrarMarca");
        this.vMain.menuRegistrarMarca.addActionListener(this);
        
        this.vMain.menuConsultarEditarMarca.setActionCommand("consultarEditarMarca");
        this.vMain.menuConsultarEditarMarca.addActionListener(this);
        
        this.vMain.menuRegistrarProveedor.setActionCommand("registrarProveedor");
        this.vMain.menuRegistrarProveedor.addActionListener(this);
    }
}
