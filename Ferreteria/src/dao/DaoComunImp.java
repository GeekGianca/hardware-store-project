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
import modelos.Factura_C_V;
import modelos.Producto;
import recursos.Comun;
import recursos.ConfigConexion;
import recursos.FactoriaMysql;

/**
 *
 * @author ***GKSoftware***
 */
public class DaoComunImp {
    
    ConfigConexion conf;

    public List<Producto> buscarProducto(String text) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM productos WHERE idproducto LIKE '%").append(text).append("%' OR nombre LIKE '%").append(text).append("%';");
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
    
    public class F_CompraNested {
        private int codigoFactura;
        private String codigoProveedor;
        private int cantidadProductos;
        private double totalCompra;
        private String fecha;

        public F_CompraNested() {
        }

        public F_CompraNested(int codigoFactura, String codigoProveedor, int cantidadProductos, double totalCompra, String fecha) {
            this.codigoFactura = codigoFactura;
            this.codigoProveedor = codigoProveedor;
            this.cantidadProductos = cantidadProductos;
            this.totalCompra = totalCompra;
            this.fecha = fecha;
        }

        public int getCodigoFactura() {
            return codigoFactura;
        }

        public void setCodigoFactura(int codigoFactura) {
            this.codigoFactura = codigoFactura;
        }

        public String getCodigoProveedor() {
            return codigoProveedor;
        }

        public void setCodigoProveedor(String codigoProveedor) {
            this.codigoProveedor = codigoProveedor;
        }

        public int getCantidadProductos() {
            return cantidadProductos;
        }

        public void setCantidadProductos(int cantidadProductos) {
            this.cantidadProductos = cantidadProductos;
        }

        public double getTotalCompra() {
            return totalCompra;
        }

        public void setTotalCompra(double totalCompra) {
            this.totalCompra = totalCompra;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }
        
    }
    
    public class V_CompraNested {
        private int codigoFactura;
        private int codigoCliente;
        private int cantidadProductos;
        private double totalCompra;

        public V_CompraNested() {
        }

        public V_CompraNested(int codigoFactura, int codigoCliente, int cantidadProductos, double totalCompra) {
            this.codigoFactura = codigoFactura;
            this.codigoCliente = codigoCliente;
            this.cantidadProductos = cantidadProductos;
            this.totalCompra = totalCompra;
        }

        public int getCodigoFactura() {
            return codigoFactura;
        }

        public void setCodigoFactura(int codigoFactura) {
            this.codigoFactura = codigoFactura;
        }

        public int getCodigoCliente() {
            return codigoCliente;
        }

        public void setCodigoCliente(int codigoCliente) {
            this.codigoCliente = codigoCliente;
        }

        public int getCantidadProductos() {
            return cantidadProductos;
        }

        public void setCantidadProductos(int cantidadProductos) {
            this.cantidadProductos = cantidadProductos;
        }

        public double getTotalCompra() {
            return totalCompra;
        }

        public void setTotalCompra(double totalCompra) {
            this.totalCompra = totalCompra;
        }
        
    }
    
    public List<F_CompraNested> listarCompras(){
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT `idfactura`, `cantidad_productos`, `total_venta`, `Proveedor_idproveedor`, `detalle_venta`,`fechahora_venta` FROM `facturas` INNER JOIN `f_compra` ON `facturas`.`idfactura` = `f_compra`.`Factura_idfactura`;");
        List<F_CompraNested> listar = null;
        try {
            listar = new ArrayList<>();
            ResultSet rs = this.conf.query(sql.toString());
            while (rs.next()) {
                F_CompraNested compras = new F_CompraNested();
                compras.setCodigoFactura(rs.getInt("idfactura"));
                compras.setCodigoProveedor(rs.getString("Proveedor_idproveedor"));
                compras.setCantidadProductos(rs.getInt("cantidad_productos"));
                compras.setTotalCompra(rs.getDouble("total_venta"));
                compras.setFecha(rs.getString("fechahora_venta"));
                listar.add(compras);
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return listar;
    }
    
    public List<V_CompraNested> listarVentas(){
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT `idfactura`, `cantidad_productos`, `total_venta`, `Cliente_idcliente`, `detalle_venta`,`fechahora_venta` FROM `facturas` INNER JOIN `f_venta` ON `facturas`.`idfactura` = `f_venta`.`Factura_idfactura`;");
        List<V_CompraNested> listar = null;
        try {
            listar = new ArrayList<>();
            ResultSet rs = this.conf.query(sql.toString());
            while (rs.next()) {
                V_CompraNested compras = new V_CompraNested();
                compras.setCodigoFactura(rs.getInt("idfactura"));
                compras.setCodigoCliente(rs.getInt("Cliente_idcliente"));
                compras.setCantidadProductos(rs.getInt("cantidad_productos"));
                compras.setTotalCompra(rs.getDouble("total_venta"));
                listar.add(compras);
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return listar;
    }
    
    public List<F_CompraNested> buscar(String like){
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT `idfactura`, `cantidad_productos`, `total_venta`, `Proveedor_idproveedor`, `detalle_venta`,`fechahora_venta` FROM `facturas` INNER JOIN `f_compra` ON `facturas`.`idfactura` = `f_compra`.`Factura_idfactura` WHERE `fechahora_venta` LIKE '%").append(like).append("%';");
        List<F_CompraNested> listar = null;
        try {
            listar = new ArrayList<>();
            ResultSet rs = this.conf.query(sql.toString());
            while (rs.next()) {
                F_CompraNested compras = new F_CompraNested();
                compras.setCodigoFactura(rs.getInt("idfactura"));
                compras.setCodigoProveedor(rs.getString("Proveedor_idproveedor"));
                compras.setCantidadProductos(rs.getInt("cantidad_productos"));
                compras.setTotalCompra(rs.getDouble("total_venta"));
                compras.setFecha(rs.getString("fechahora_venta"));
                listar.add(compras);
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return listar;
    }
    
    public List<V_CompraNested> buscarVentas(String like){
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT `idfactura`, `cantidad_productos`, `total_venta`, `Cliente_idcliente`, `detalle_venta`,`fechahora_venta` FROM `facturas` INNER JOIN `f_venta` ON `facturas`.`idfactura` = `f_venta`.`Factura_idfactura` WHERE `Cliente_idcliente` LIKE '%").append(like).append("%';");
        List<V_CompraNested> listar = null;
        try {
            listar = new ArrayList<>();
            ResultSet rs = this.conf.query(sql.toString());
            while (rs.next()) {
                V_CompraNested compras = new V_CompraNested();
                compras.setCodigoFactura(rs.getInt("idfactura"));
                compras.setCodigoCliente(rs.getInt("Cliente_idcliente"));
                compras.setCantidadProductos(rs.getInt("cantidad_productos"));
                compras.setTotalCompra(rs.getDouble("total_venta"));
                listar.add(compras);
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conf.close();
        }
        return listar;
    }
    
    public boolean insertar(Factura_C_V clazzcommon) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean guardar = false;
        try {
            String sql = String.format("INSERT INTO `f_venta`(`Factura_idfactura`, `Cliente_idcliente`) VALUES (%s,'%s')",
                    clazzcommon.getIdFactura(),
                    String.valueOf(clazzcommon.getId())
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
}
