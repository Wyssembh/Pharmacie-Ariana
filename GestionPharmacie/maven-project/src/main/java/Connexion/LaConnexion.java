/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connexion;

/**
 *
 * @author nidha
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author nidha
 */
public class LaConnexion {
    private static Connection con;
   private static String user="root";
    private static String password="";
    
    /*public static Connection seConnecter(){
        if(con==null){
            try {
                String host = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
                con=DriverManager.getConnection("jdbc:mysql://" + host + ":3306/pharmacie",user,password);
                System.out.println("Connexion établie");
            }
            catch(SQLException ex){
                System.out.println("Bd non trouvé ou problème d'identification "+ex.getMessage());
            }
        }
     return con;
    }*/
    public static Connection seConnecter(){
        if(con==null){
            try {
                con=DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacie",user,password);
                System.out.println("Connexion établie");
            }
            catch(SQLException ex){
                System.out.println("Bd non trouvé ou problème d'identification "+ex.getMessage());
            }
        }
     return con;
    }

}