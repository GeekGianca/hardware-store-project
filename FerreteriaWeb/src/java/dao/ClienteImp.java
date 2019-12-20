/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Cliente;
import recursos.Comun;
import recursos.ConfigConexion;
import recursos.FactoriaMysql;

/**
 *
 * @author ***GKSoftware***
 */
public class ClienteImp implements IDataAccessObject<Cliente> {

    ConfigConexion conf;

    @Override
    public List<Cliente> lista() {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM cliente;");
        List<Cliente> listar = null;
        try {
            listar = new ArrayList<>();
            ResultSet rs = this.conf.query(sql.toString());
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdentificacion(rs.getInt("idCliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setTelefono(rs.getString("telefono"));
                listar.add(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return listar;
    }

    @Override
    public Cliente seleccionar(int id) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        Cliente cliente = null;
        String sql = String.format("SELECT * FROM cliente WHERE idCliente = %s", id);
        try {
            ResultSet rs = this.conf.query(sql);
            cliente = new Cliente();
            while (rs.next()) {
                cliente.setIdentificacion(rs.getInt("idCliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setTelefono(rs.getString("telefono"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return cliente;
    }

    @Deprecated
    @Override
    public Cliente seleccionar(String id) {
        return null;
    }

    @Override
    public boolean actualizar(Cliente clazzcommon) {
        return false;
    }

    @Override
    public boolean insertar(Cliente clazzcommon) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean guardar = false;
        try {
            String sql = String.format("INSERT INTO `cliente`(`idCliente`, `nombre`, `direccion`, `telefono`) VALUES (%s,'%s','%s','%s');",
                    clazzcommon.getIdentificacion(),
                    clazzcommon.getNombre(),
                    clazzcommon.getDireccion(),
                    clazzcommon.getTelefono()
            );
            Comun.SENTENCIA_REALIZADA = sql;
            guardar = this.conf.execute(sql);
        } catch (Exception e) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        } finally {
            this.conf.close();
        }
        return guardar;
    }

    @Override
    public boolean eliminar(int id) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean eliminar = false;
        try {
            String sql = String.format("DELETE FROM cliente WHERE idCliente=%s", id);
            eliminar = this.conf.execute(sql);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        } finally {
            this.conf.close();
        }
        return eliminar;
    }

    @Override
    public boolean eliminar(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
