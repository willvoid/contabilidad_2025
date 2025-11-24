/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.contabilidad_2025.modelo.dao;

import com.lp2.contabilidad_2025.modelo.entidad.CuentaContable;
import com.lp2.contabilidad_2025.modelo.entidad.TipoCuenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CuentaContableCrudImpl implements Crud<CuentaContable>{
    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public CuentaContableCrudImpl() {
        Conexion conectar = new Conexion();
        conec =  conectar.conectarBD();
    }
    
    
    

    @Override
    public void insertar(CuentaContable m) {
        try {
            //Preparar sentencia
            String sql="insert into cuentas_contables (nombre,codigo,fk_tipo) values(?,?,?)";
            sentencia = conec.prepareStatement(sql);
            //Asginar valor a los parametros
            sentencia.setString(1, m.getNombre());
            sentencia.setString(2, m.getCodigo());
            sentencia.setInt(3, m.getTipo().getId());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CuentaContableCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(CuentaContable obj) {
        try {
            String sql="update cuentas_contables set nombre=?, codigo=?, fk_tipo=? where id_cuentas_contables=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, obj.getNombre());
            sentencia.setString(2, obj.getCodigo());
            sentencia.setInt(3, obj.getTipo().getId());
            sentencia.setInt(4, obj.getId());
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CuentaContableCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminar(CuentaContable obj) {
        try {
            //Prepara sentencia sql
            String sql="delete from cuentas_contables where id_cuentas_contables=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1,obj.getId());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CuentaContableCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @Override
    public List<CuentaContable> listar(String textoBuscado) {
        System.out.println("texto buscado "+ textoBuscado);
        ArrayList<CuentaContable> lista = new ArrayList<>();
        try {
            String sql="SELECT c.*, tc.nombre as nombre_tipo\n" +
                       " FROM public.cuentas_contables c\n" +
                       " INNER JOIN public.tipo_cuentas tc ON tc.id_tipo_cuentas = c.fk_tipo\n"
                    +  " WHERE c.nombre ilike ? ORDER BY c.nombre DESC";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" +textoBuscado+"%");
            ResultSet rs = sentencia.executeQuery();
            
            //recorrer una lista
            while(rs.next()){
                TipoCuenta tipo = new TipoCuenta();
                CuentaContable cuentas_contables = new CuentaContable();
                cuentas_contables.setId(rs.getInt("id_cuentas_contables"));
                cuentas_contables.setNombre(rs.getString("nombre"));
                cuentas_contables.setCodigo(rs.getString("codigo"));
                
                //Asignar valor a TipoCuenta
                tipo.setId(rs.getInt("fk_tipo"));
                tipo.setNombre(rs.getString("nombre_tipo"));
                cuentas_contables.setTipo(tipo);
                
                
                lista.add(cuentas_contables);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CuentaContableCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
}
