/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ircbot;

import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Garrett
 */
public class JavaToMysql {
    
    private String addr, dbName, driver, userName, password;
    
    public JavaToMysql(String address, String port, String user, String pass, String DBName) {
        driver   = "com.mysql.jdbc.Driver";        
        addr = "jdbc:mysql://" + address + ":" + port + "/";
        userName = user;
        password = pass;
        dbName = DBName;
        
        try {
            Class.forName(driver).newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JavaToMysql.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(JavaToMysql.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(JavaToMysql.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
    
    public Vector<String> select() {
        Vector<String> registeredUsers = new Vector<String>(1);
        try {
            Connection conn = DriverManager.getConnection(addr+dbName,userName,password);
            
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery("SELECT user_nicename FROM subwp_users");
            
            while (res.next()) {
                String usr = res.getString("user_nicename");
                registeredUsers.addElement(usr);
                //System.out.println(usr);
            }
            
            conn.close();
            System.out.println("\n\n\n\n\n\nTesting Selection\n\n\n\n");
        } catch (SQLException e) {
            Logger.getLogger(JavaToMysql.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return registeredUsers;
    }
    
    public boolean selectuser(String user) {
        try {
            Connection conn = DriverManager.getConnection(addr+dbName,userName,password);
            
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery("SELECT subwp_users.user_nicename "
                    + "FROM subwp_users, subwp_posts "
                    + "WHERE subwp_users.user_nicename='" + user + "' AND subwp_posts.post_author=subwp_users.ID AND subwp_posts.post_status='publish'");
            
            if(res.next()) {
                conn.close();
                return true;
            }
            
            conn.close();
            //System.out.println("\n\n\n\n\n\nTesting Selection\n\n\n\n");
        } catch (SQLException e) {
            Logger.getLogger(JavaToMysql.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return false;
    }
}
