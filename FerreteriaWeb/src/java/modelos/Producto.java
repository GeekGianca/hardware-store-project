/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author ***GKSoftware***
 */
public class Producto {
    private String idproducto;
    private String nombre;
    private double precioVenta;
    private double precioCompra;
    private String fechaRegistro;
    private int disponibilidad;
    private int categoria;

    public Producto() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY", Locale.ENGLISH);
        this.fechaRegistro = sdf.format(d);
        
    }

    public String getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(String idproducto) {
        this.idproducto = idproducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(int disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Producto{" + "idproducto=" + idproducto + ", nombre=" + nombre + ", precioVenta=" + precioVenta + ", precioCompra=" + precioCompra + ", fechaRegistro=" + fechaRegistro + ", disponibilidad=" + disponibilidad + ", categoria=" + categoria + '}';
    }
    
}
