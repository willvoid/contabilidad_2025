/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.contabilidad_2025.modelo.dao;

import com.lp2.contabilidad_2025.modelo.entidad.Asiento;
import com.lp2.contabilidad_2025.modelo.entidad.TipoCuenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsientoCrudImpl implements Crud<Asiento>{
    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public AsientoCrudImpl() {
        Conexion conectar = new Conexion();
        conec =  conectar.conectarBD();
    }
    
    
    

    @Override
    public void insertar(Asiento m) {
        try {
            // Convertir java.util.Date a java.sql.Date
            java.util.Date fecha = m.getFecha(); // Obtener fecha como java.util.Date
            java.sql.Date fechaSQL = new java.sql.Date(fecha.getTime()); // Convertir a java.sql.Date
            
            //Preparar sentencia
            String sql="insert into asientos (fecha,descripcion) values(?,?)";
            sentencia = conec.prepareStatement(sql);
            //Asginar valor a los parametros
            sentencia.setDate(1, fechaSQL);
            sentencia.setString(2, m.getDescripcion());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AsientoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(Asiento obj) {
        try {
            // Convertir java.util.Date a java.sql.Date
            java.util.Date fecha = obj.getFecha(); // Obtener fecha como java.util.Date
            java.sql.Date fechaSQL = new java.sql.Date(fecha.getTime()); // Convertir a java.sql.Date
            
            String sql="update asientos set fecha=?, descripcion=? where id_asientos=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setDate(1, fechaSQL);
            sentencia.setString(2, obj.getDescripcion());
            sentencia.setInt(3, obj.getId());
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AsientoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminar(Asiento obj) {
        try {
            //Prepara sentencia sql
            String sql="delete from asientos where id_asiento=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1,obj.getId());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AsientoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    public Asiento leerAsiento(String textoBuscado) {
        System.out.println("texto buscado "+ textoBuscado);
        Asiento asiento = null;

        try {
            Integer idAsiento = Integer.valueOf(textoBuscado);
            String sql = "SELECT * FROM asientos WHERE id_asientos = ?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setInt(1, idAsiento);
            ResultSet rs = sentencia.executeQuery();

            // Solo obtenemos el primer resultado
            if(rs.next()){
                asiento = new Asiento();
                asiento.setId(rs.getInt("id_asientos"));
                asiento.setFecha(rs.getDate("fecha"));
                asiento.setDescripcion(rs.getString("descripcion"));
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: El texto buscado no es un número válido");
        } catch (SQLException ex) {
            Logger.getLogger(AsientoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return asiento;
    }
    
    @Override
    public List<Asiento> listar(String textoBuscado) {
        System.out.println("texto buscado "+ textoBuscado);
        ArrayList<Asiento> lista = new ArrayList<>();
        try {
            String sql="SELECT * FROM asientos WHERE fecha ilike ?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" +textoBuscado+"%");
            ResultSet rs = sentencia.executeQuery();
            
            //recorrer una lista
            while(rs.next()){
                TipoCuenta tipo = new TipoCuenta();
                Asiento asiento = new Asiento();
                asiento.setId(rs.getInt("id_asientos"));
                asiento.setFecha(rs.getDate("fecha"));
                asiento.setDescripcion(rs.getString("descripcion"));
                
                lista.add(asiento);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AsientoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
}
