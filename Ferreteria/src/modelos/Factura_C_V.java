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
public class Factura_C_V {
    private int idFactura;
    private int id;

    public Factura_C_V(int idFactura, int id) {
        this.idFactura = idFactura;
        this.id = id;
    }

    public Factura_C_V() {
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FacturaF_V{" + "idFactura=" + idFactura + ", id=" + id + '}';
    }
    
}
