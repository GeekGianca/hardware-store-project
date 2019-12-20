/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
/**
 *
 * @author ***GKSoftware
 ***
 * @param <ClazzCommon>
 */
public interface IDataAccessObject<ClazzCommon> {
    
    public List<ClazzCommon> lista();
    public ClazzCommon seleccionar(int id);
    public ClazzCommon seleccionar(String id);
    public boolean actualizar(ClazzCommon clazzcommon);
    public boolean insertar(ClazzCommon clazzcommon);
    public boolean eliminar(int id);
    public boolean eliminar(String id);
}
