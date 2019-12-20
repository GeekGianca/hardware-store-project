/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.DetalleFactura;
import recursos.Comun;
import recursos.ConfigConexion;
import recursos.FactoriaMysql;

/**
 *
 * @author ***GKSoftware***
 */
public class DetalleFacturaImp implements IDataAccessObject<DetalleFactura> {

    ConfigConexion conf;

    @Deprecated
    @Override
    public List<DetalleFactura> lista() {
        return null;
    }

    @Deprecated
    @Override
    public DetalleFactura seleccionar(int id) {
        return null;
    }

    @Deprecated
    @Override
    public DetalleFactura seleccionar(String id) {
        return null;
    }

    @Deprecated
    @Override
    public boolean actualizar(DetalleFactura clazzcommon) {
        return false;
    }

    @Override
    public boolean insertar(DetalleFactura clazzcommon) {
        this.conf = FactoriaMysql.getInstanceOpenConexion(FactoriaMysql.MYSQL);
        boolean guardar = false;
        try {
            String sql = String.format("INSERT INTO `detalle_factura`(`Productos_idproducto`, `Facturas_idfactura`) VALUES ('%s',%s)",
                    clazzcommon.getIdProducto(),
                    clazzcommon.getIdFactura()
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    @Override
    public boolean eliminar(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
