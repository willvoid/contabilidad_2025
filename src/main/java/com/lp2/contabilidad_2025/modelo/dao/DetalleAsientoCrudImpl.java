/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.contabilidad_2025.modelo.dao;

import com.lp2.contabilidad_2025.modelo.entidad.Asiento;
import com.lp2.contabilidad_2025.modelo.entidad.CuentaContable;
import com.lp2.contabilidad_2025.modelo.entidad.DetalleAsiento;
import com.lp2.contabilidad_2025.modelo.entidad.TipoCuenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DetalleAsientoCrudImpl implements Crud <DetalleAsiento>{
    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public DetalleAsientoCrudImpl() {
        Conexion conectar = new Conexion();
        conec =  conectar.conectarBD();
    }
    
    
    

    @Override
    public void insertar(DetalleAsiento m) {
        try {
            //Preparar sentencia
            String sql="insert into detalle_asientos (fk_cuentas,fk_asientos,debe,haber) values(?,?,?,?)";
            sentencia = conec.prepareStatement(sql);
            //Asginar valor a los parametros
            sentencia.setInt(1, m.getCuenta().getId());
            sentencia.setInt(2, m.getFkAsiento());
            sentencia.setDouble(3, m.getDebe());
            sentencia.setDouble(4, m.getHaber());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DetalleAsientoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(DetalleAsiento obj) {
        try {
            String sql="update detalle_asientos set fk_cuentas=?,debe=?,haber=? where a=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setInt(1, obj.getCuenta().getId());
            sentencia.setDouble(2, obj.getDebe());
            sentencia.setDouble(3, obj.getHaber());
            sentencia.setInt(4, obj.getId());
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DetalleAsientoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminar(DetalleAsiento obj) {
        try {
            //Prepara sentencia sql
            String sql="delete from detalle_asientos where id_detalle=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1,obj.getId());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DetalleAsientoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public int obtenerValorActualSecuencia() {
        int numeroActual = 0;
        String sql = "SELECT last_value FROM asientos_id_asientos_seq"; // Consulta para obtener el valor actual
        try {
            PreparedStatement ps = conec.prepareStatement(sql); 

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                numeroActual = rs.getInt(1); // Obtener el valor de la columna
            }
        } catch (SQLException ex) {
            Logger.getLogger(DetalleAsientoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numeroActual;
    }

    @Override
    public ArrayList<DetalleAsiento> listar(String textoBuscado) {
        System.out.println("texto buscado "+ textoBuscado);
        Integer idBuscado = Integer.valueOf(textoBuscado);
        ArrayList<DetalleAsiento> lista = new ArrayList<>();
        try {
            String sql="SELECT da.*, c.nombre as nombre_cuenta FROM detalle_asientos da "
                    + "INNER JOIN cuentas_contables c ON c.id_cuentas_contables = da.fk_cuentas "
                    + "WHERE fk_asientos = ?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setInt(1, idBuscado);
            ResultSet rs = sentencia.executeQuery();
            
            //recorrer una lista
            while(rs.next()){
                CuentaContable cuenta = new CuentaContable();
                DetalleAsiento asiento = new DetalleAsiento();
                asiento.setId(rs.getInt("id_detalle"));
                asiento.setFkAsiento(rs.getInt("fk_asientos"));
                asiento.setDebe(rs.getDouble("debe"));
                asiento.setHaber(rs.getDouble("haber"));
                
                //Asignar valor a TipoCuenta
                cuenta.setId(rs.getInt("fk_cuentas"));
                cuenta.setNombre(rs.getString("nombre_cuenta"));
                asiento.setCuenta(cuenta);
                
                lista.add(asiento);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DetalleAsientoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
}
