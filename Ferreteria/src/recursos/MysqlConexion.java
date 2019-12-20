/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recursos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gianc
 */
public class MysqlConexion extends ConfigConexion {

    public MysqlConexion(String params[]) {
        this.params = params;
        this.open();
    }

    @Override
    Connection open() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+this.params[0]+"?autoReconnect=true&useSSL=false", this.params[1], this.params[2]);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MysqlConexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this.conn;
    }
}
