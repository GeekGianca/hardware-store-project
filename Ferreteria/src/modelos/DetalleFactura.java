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
public class DetalleFactura {
    private String idProducto;
    private int idFactura;

    public DetalleFactura() {
    }

    public DetalleFactura(String idProducto, int idFactura) {
        this.idProducto = idProducto;
        this.idFactura = idFactura;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    @Override
    public String toString() {
        return "DetalleFactura{" + "idProducto=" + idProducto + ", idFactura=" + idFactura + '}';
    }    
    
}
