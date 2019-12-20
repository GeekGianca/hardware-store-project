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
import modelos.Factura;
import recursos.Comun;
import recursos.ConfigConexion;
import recursos.FactoriaMysql;

/**
 *
 * @author ***GKSoftware***
 */
public class FacturaImp implements IDataAccessObject<Factura>{

    ConfigConexion conf;
    
    @Override
    public List<Factura> lista() {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM facturas;");
        List<Factura> listar = null;
        try {
            listar = new ArrayList<>();
            ResultSet rs = this.conf.query(sql.toString());
            while (rs.next()) {
                Factura factura = new Factura();
                factura.setIdfactura(rs.getInt("idfactura"));
                factura.setNoventa(rs.getString("noventa"));
                factura.setCantidadProductos(rs.getInt("cantidad_productos"));
                factura.setTotalVenta(rs.getDouble("total_venta"));
                factura.setFechaVenta(rs.getString("fechahorA_venta"));
                listar.add(factura);
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return listar;
    }

    @Override
    public Factura seleccionar(int id) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        Factura factura = null;
        String sql = String.format("SELECT * FROM facturas WHERE idfactura = %s", id);
        try {
            ResultSet rs = this.conf.query(sql);
            factura = new Factura();
            while (rs.next()) {
                factura.setIdfactura(rs.getInt("idfactura"));
                factura.setNoventa(rs.getString("noventa"));
                factura.setCantidadProductos(rs.getInt("cantidad_productos"));
                factura.setTotalVenta(rs.getDouble("total_venta"));
                factura.setFechaVenta(rs.getString("fechahorA_venta"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return factura;
    }

    @Deprecated
    @Override
    public Factura seleccionar(String id) {
        return null;
    }

    @Deprecated
    @Override
    public boolean actualizar(Factura clazzcommon) {
        return false;
    }

    @Override
    public boolean insertar(Factura clazzcommon) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean guardar = false;
        try {
            String sql = String.format("INSERT INTO `facturas`(`idfactura`, `noventa`, `cantidad_productos`, `total_venta`, `fechahora_venta`, `detalle_venta`) VALUES (%s,'%s',%s,%s,(SELECT NOW()),'%s')",
                    clazzcommon.getIdfactura(),
                    clazzcommon.getNoventa(),
                    clazzcommon.getCantidadProductos(),
                    clazzcommon.getTotalVenta(),
                    clazzcommon.getDetalleVenta()
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
        return false;
    }
    
}
