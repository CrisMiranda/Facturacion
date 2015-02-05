/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;
//MODIFICADO POR DANIEL PAUCAR
//conectado a la base de datos FACTURACION
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris
 */
public class Conexion {
    Connection connect = null;
      public Connection conectar(){
        try {
            Class.forName("org.postgresql.Driver");
            connect=DriverManager.getConnection("jdbc:postgresql://localhost:5432/minimarket","postgres","oracle");
            //JOptionPane.showMessageDialog(null, "Felicitaciones");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error"+ex);
        }
        return connect;
      } 
}
