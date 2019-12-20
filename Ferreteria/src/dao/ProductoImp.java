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
import modelos.Producto;
import recursos.Comun;
import recursos.ConfigConexion;
import recursos.FactoriaMysql;

/**
 *
 * @author ***GKSoftware***
 */
public class ProductoImp implements IDataAccessObject<Producto> {

    ConfigConexion conf;

    @Override
    public List<Producto> lista() {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM productos;");
        List<Producto> listar = null;
        try {
            listar = new ArrayList<>();
            ResultSet rs = this.conf.query(sql.toString());
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdproducto(rs.getString("idproducto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecioVenta(rs.getDouble("precio_venta"));
                producto.setPrecioCompra(rs.getDouble("precio_compra"));
                producto.setFechaRegistro(rs.getString("fecha_registro"));
                producto.setDisponibilidad(rs.getInt("disponibilidad"));
                producto.setCategoria(rs.getInt("categoria_idcategoria"));
                listar.add(producto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return listar;
    }

    @Override
    public Producto seleccionar(int id) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        Producto producto = null;
        String sql = String.format("SELECT * FROM productos WHERE idproducto = %s", id);
        try {
            ResultSet rs = this.conf.query(sql);
            producto = new Producto();
            while (rs.next()) {
                producto.setIdproducto(rs.getString("idproducto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecioVenta(rs.getDouble("precio_venta"));
                producto.setPrecioCompra(rs.getDouble("precio_compra"));
                producto.setFechaRegistro(rs.getString("fecha_registro"));
                producto.setDisponibilidad(rs.getInt("disponibilidad"));
                producto.setCategoria(rs.getInt("categoria_idcategoria"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return producto;
    }

    @Override
    public boolean actualizar(Producto clazzcommon) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean editar = false;
        String sql = String.format("UPDATE `productos` SET `disponibilidad`=%s WHERE `idproducto` = %s",
                clazzcommon.getDisponibilidad(), clazzcommon.getIdproducto());
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
    public boolean insertar(Producto producto) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean guardar = false;
        try {
            String sql = String.format("INSERT INTO `productos`(`idproducto`, `nombre`, `precio_venta`, `precio_compra`, `fecha_registro`, `disponibilidad`, `categoria_idcategoria`) VALUES (%s,'%s',%s,%s,(SELECT NOW()),%s,%s)",
                    producto.getIdproducto(),
                    producto.getNombre(),
                    producto.getPrecioVenta(),
                    producto.getPrecioCompra(),
                    producto.getDisponibilidad(),
                    producto.getCategoria()
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
        return false;
    }

    @Override
    public boolean eliminar(String id) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean eliminar = false;
        try {
            String sql = String.format("DELETE FROM productos WHERE idproducto='%s'", id);
            eliminar = this.conf.execute(sql);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        } finally {
            this.conf.close();
        }
        return eliminar;
    }

    @Override
    public Producto seleccionar(String id) {
        return null;
    }

}
