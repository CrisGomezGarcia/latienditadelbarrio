package Controlador;

import Vista.V_JDialog_AgregarProductoACompra;

public class C_JDialog_AgregarProductoACompra {
    private final V_JDialog_AgregarProductoACompra dlg;
    
    public C_JDialog_AgregarProductoACompra(V_JDialog_AgregarProductoACompra dlg) {
        this.dlg = dlg;
        cargarDialog();
    }
    
    private void cargarDialog() {
        dlg.setTitle("Seleccionar Producto");
        
    }
}
