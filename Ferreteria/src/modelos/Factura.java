/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ***GKSoftware***
 */
public class Factura {
    private int idfactura;
    private String noventa;
    private int cantidadProductos;
    private double totalVenta;
    private String fechaVenta;
    private List<Producto> detalleVenta;

    public Factura() {
        this.idfactura = 0;
        this.noventa = "";
        this.cantidadProductos = 0;
        this.totalVenta = 0;
        this.fechaVenta = "";
        this.detalleVenta = new ArrayList<>();
    }

    public int getIdfactura() {
        return idfactura;
    }

    public void setIdfactura(int idfactura) {
        this.idfactura = idfactura;
    }

    public String getNoventa() {
        return noventa;
    }

    public void setNoventa(String noventa) {
        this.noventa = noventa;
    }

    public int getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(int cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public List<Producto> getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(List<Producto> detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    @Override
    public String toString() {
        return "Factura{" + "idfactura=" + idfactura + ", noventa=" + noventa + ", cantidadProductos=" + cantidadProductos + ", totalVenta=" + totalVenta + ", fechaVenta=" + fechaVenta + ", detalleVenta=" + detalleVenta + '}';
    }
    
}
