/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Factura_C_V;
import recursos.Comun;
import recursos.ConfigConexion;
import recursos.FactoriaMysql;

/**
 *
 * @author ***GKSoftware***
 */
public class Factura_C_VImp implements IDataAccessObject<Factura_C_V> {

    ConfigConexion conf;

    @Deprecated
    @Override
    public List<Factura_C_V> lista() {
        return null;
    }

    @Deprecated
    @Override
    public Factura_C_V seleccionar(int id) {
        return null;
    }

    @Deprecated
    @Override
    public Factura_C_V seleccionar(String id) {
        return null;
    }

    @Deprecated
    @Override
    public boolean actualizar(Factura_C_V clazzcommon) {
        return false;
    }

    @Override
    public boolean insertar(Factura_C_V clazzcommon) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean guardar = false;
        try {
            String sql = String.format("INSERT INTO `f_compra`(`Factura_idfactura`, `Proveedor_idproveedor`) VALUES (%s,'%s')",
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

    @Deprecated
    @Override
    public boolean eliminar(int id) {
        return false;
    }

    @Deprecated
    @Override
    public boolean eliminar(String id) {
        return false;
    }

}
