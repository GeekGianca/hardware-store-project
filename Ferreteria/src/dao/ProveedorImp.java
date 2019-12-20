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
import modelos.Proveedor;
import recursos.Comun;
import recursos.ConfigConexion;
import recursos.FactoriaMysql;

/**
 *
 * @author ***GKSoftware***
 */
public class ProveedorImp implements IDataAccessObject<Proveedor> {

    ConfigConexion conf;

    @Override
    public List<Proveedor> lista() {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM proveedor;");
        Comun.SENTENCIA_REALIZADA = sql.toString();
        List<Proveedor> listar = null;
        try {
            listar = new ArrayList<>();
            ResultSet rs = this.conf.query(sql.toString());
            while (rs.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setIdproveedor(rs.getString("idproveedor"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setCiudad(rs.getString("ciudad"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setNombreContacto(rs.getString("nombre_contacto"));
                listar.add(proveedor);
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return listar;
    }

    @Override
    public Proveedor seleccionar(int id) {
        return null;
    }

    @Override
    public boolean actualizar(Proveedor clazzcommon) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean editar = false;
        String sql = String.format("UPDATE `proveedor` SET `nombre`='%s',`ciudad`='%s',`telefono`='%s',`nombre_contacto`='%s' WHERE `idproveedor` = %s", 
                clazzcommon.getNombre(), 
                clazzcommon.getCiudad(), 
                clazzcommon.getTelefono(), 
                clazzcommon.getNombreContacto(), 
                clazzcommon.getIdproveedor()
        );
        Comun.SENTENCIA_REALIZADA = sql;
        try {
            editar = this.conf.execute(sql);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return editar;
    }

    @Override
    public boolean insertar(Proveedor clazzcommon) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean guardar = false;
        try{
            String sql = String.format("INSERT INTO `proveedor`(`idproveedor`, `nombre`, `ciudad`, `telefono`, `nombre_contacto`) VALUES ('%s','%s','%s','%s','%s')",
                    clazzcommon.getIdproveedor(),
                    clazzcommon.getNombre(), 
                    clazzcommon.getCiudad(),
                    clazzcommon.getTelefono(),
                    clazzcommon.getNombreContacto());
            Comun.SENTENCIA_REALIZADA = sql;
            guardar = this.conf.execute(sql);
        }catch(Exception e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        } finally{
            this.conf.close();
        }
        return guardar;
    }

    @Override
    public boolean eliminar(int id) {
        return false;
    }

    @Override
    public boolean eliminar(String id) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean eliminar = false;
        try{
            String sql = String.format("DELETE FROM proveedor WHERE idproveedor='%s'", id);
            Comun.SENTENCIA_REALIZADA = sql;
            eliminar = this.conf.execute(sql);
        }catch(Exception e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }finally{
            this.conf.close();
        }
        return eliminar;
    }

    @Override
    public Proveedor seleccionar(String id) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        Proveedor proveedor = null;
        String sql = String.format("SELECT * FROM proveedor WHERE idproveedor=%s", id);
        Comun.SENTENCIA_REALIZADA = sql;
        try {
            ResultSet rs = this.conf.query(sql);
            proveedor = new Proveedor();
            while (rs.next()) {
                proveedor.setIdproveedor(rs.getString("idproveedor"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setCiudad(rs.getString("ciudad"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setNombreContacto(rs.getString("nombre_contacto"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return proveedor;
    }

}
