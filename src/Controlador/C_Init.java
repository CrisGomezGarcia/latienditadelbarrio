package Controlador;

import Modelo.M_ConexionBD;
import Vista.V_Main;

public class C_Init {

    public static void main(String[] args) {
        V_Main ventana = new V_Main();
        new C_Main(ventana).CargarFormulario();

        // 👉 Añades listener para cuando el usuario cierre la ventana principal
        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.out.println("Cerrando pool Hikari...");
                M_ConexionBD.cerrarPool();
                System.exit(0); // Salir de la app completamente
            }
        });
    }
}
