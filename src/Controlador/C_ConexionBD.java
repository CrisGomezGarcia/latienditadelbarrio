/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cristian Gomez
 */
public class C_ConexionBD {
    private static final String url = "jdbc:mysql://localhost:3306/latienditadelbarrio";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String user = "root";
    private static final String password = "";
    
    private static C_ConexionBD instance = null;

    private static Connection conexion = null;

    private C_ConexionBD() {

    }

    public static C_ConexionBD getInstance() {
        if (instance == null) {
            instance = new C_ConexionBD();
        }
        return instance;
    }

    public Connection openConnection() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName(C_ConexionBD.driver);
                conexion = DriverManager.getConnection(url, user, password);
                System.out.println("Se conectó a la base de datos");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("No se pudo conectar a la base de datos");
            Logger.getLogger(C_ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conexion;
    }

    public void closeInstance() {
        instance = null;
    }

    public void closeConnection() {
        try {
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(C_ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
