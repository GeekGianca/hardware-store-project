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
import modelos.Categoria;
import recursos.Comun;
import recursos.ConfigConexion;
import recursos.FactoriaMysql;

/**
 *
 * @author ***GKSoftware***
 */
public class CategoriaImp implements IDataAccessObject<Categoria>{

    ConfigConexion conf;
    
    @Override
    public List<Categoria> lista() {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM categoria;");
        List<Categoria> listar = null;
        try {
            listar = new ArrayList<>();
            ResultSet rs = this.conf.query(sql.toString());
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setCategoria(rs.getInt("idcategoria"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                listar.add(categoria);
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return listar;
    }

    @Override
    public Categoria seleccionar(int id) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        Categoria categoria = null;
        String sql = String.format("SELECT * FROM categoria WHERE idcategoria=%s", id);
        try {
            ResultSet rs = this.conf.query(sql);
            categoria = new Categoria();
            while (rs.next()) {
                categoria.setCategoria(rs.getInt("idcategoria"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setCantidadDisponible(rs.getInt("cantidad_disponible"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return categoria;
    }

    @Override
    public boolean actualizar(Categoria clazzcommon) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean editar = false;
        String sql = String.format("UPDATE `categoria` SET `nombre`='%s',`cantidad_disponible`='%s' WHERE `idcategoria` = %s", 
                clazzcommon.getNombre(), clazzcommon.getNombre(), clazzcommon.getCantidadDisponible(), clazzcommon.getCategoria());
        Comun.SENTENCIA_REALIZADA = sql;
        try {
            editar = this.conf.execute(sql);
        } catch (Exception ex) {
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return editar;
    }

    @Override
    public boolean insertar(Categoria clazzcommon) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean guardar = false;
        try{
            String sql = String.format("INSERT INTO `categoria`(`idcategoria`, `nombre`, `cantidad_disponible`) VALUES (%s,'%s',%s)",
                    clazzcommon.getCategoria(), clazzcommon.getNombre(), clazzcommon.getCantidadDisponible());
            Comun.SENTENCIA_REALIZADA = sql;
            guardar = this.conf.execute(sql);
        }catch(Exception e){
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, e);
        } finally{
            this.conf.close();
        }
        return guardar;
    }

    @Override
    public boolean eliminar(int id) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean eliminar = false;
        try{
            String sql = String.format("DELETE FROM categoria WHERE idcategoria=%s", id);
            eliminar = this.conf.execute(sql);
        }catch(Exception e){
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.conf.close();
        }
        return eliminar;
    }

    @Override
    public boolean eliminar(String id) {
        return false;
    }

    @Override
    public Categoria seleccionar(String id) {
        return null;
    }
    
}
