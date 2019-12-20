/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

/**
 *
 * @author ***GKSoftware***
 */
public class Proveedor {
    private String idproveedor;
    private String nombre;
    private String ciudad;
    private String telefono;
    private String nombreContacto;

    public Proveedor(String idproveedor, String nombre, String ciudad, String telefono, String nombreContacto) {
        this.idproveedor = idproveedor;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.nombreContacto = nombreContacto;
    }

    public Proveedor() {
    }

    public String getIdproveedor() {
        return idproveedor;
    }

    public void setIdproveedor(String idproveedor) {
        this.idproveedor = idproveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }
    
}
