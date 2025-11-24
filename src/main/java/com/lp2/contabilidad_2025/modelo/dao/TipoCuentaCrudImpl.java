/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.contabilidad_2025.modelo.dao;


import com.lp2.contabilidad_2025.modelo.entidad.TipoCuenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cmendieta
 */
public class TipoCuentaCrudImpl implements Crud<TipoCuenta> {

    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public TipoCuentaCrudImpl() {
        Conexion conectar = new Conexion();
        conec = conectar.conectarBD();
    }

    @Override
    public void insertar(TipoCuenta m) {
        try {
            //Preparar sentencia
            String sql = "insert into tipo_cuentas (nombre,descripcion) values(?,?)";
            sentencia = conec.prepareStatement(sql);
            //Asginar valor a los parametros
            sentencia.setString(1, m.getNombre());
            sentencia.setString(2, m.getDescripcion());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TipoCuentaCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(TipoCuenta obj) {
        try {
            String sql = "update tipo_cuentas set nombre=?, descripcion=? where id_tipo_cuentas=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, obj.getNombre());
            sentencia.setString(2, obj.getDescripcion());
            sentencia.setInt(3, obj.getId());
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TipoCuentaCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminar(TipoCuenta obj) {
        try {
            //Prepara sentencia sql
            String sql = "delete from tipo_cuentas where id_tipo_cuentas=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1, obj.getId());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TipoCuentaCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<TipoCuenta> listar(String textoBuscado) {
        System.out.println("texto buscado " + textoBuscado);
        ArrayList<TipoCuenta> lista = new ArrayList<>();
        try {
            String sql = "select * from tipo_cuentas where nombre ilike ?  order by nombre asc";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" + textoBuscado + "%");
            ResultSet rs = sentencia.executeQuery();

            //recorrer una lista
            while (rs.next()) {
                TipoCuenta TipoCuenta = new TipoCuenta();
                TipoCuenta.setId(rs.getInt("id_tipo_cuentas"));
                TipoCuenta.setNombre(rs.getString("nombre"));
                TipoCuenta.setDescripcion(rs.getString("descripcion"));
                lista.add(TipoCuenta);
            }

        } catch (SQLException ex) {
            Logger.getLogger(TipoCuentaCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

}
