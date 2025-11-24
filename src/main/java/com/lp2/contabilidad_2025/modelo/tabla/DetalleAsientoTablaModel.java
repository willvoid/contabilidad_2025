/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.contabilidad_2025.modelo.tabla;



import com.lp2.contabilidad_2025.modelo.entidad.DetalleAsiento;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class DetalleAsientoTablaModel extends AbstractTableModel{
    List<DetalleAsiento> lista;
    private String[] columnas ={"ID","CUENTA","VALOR"};
    
    public void setLista(List<DetalleAsiento> lista){
        // Inicializamos las lista de cuentas
        this.lista = lista;
    }

    @Override
    public int getRowCount() {
        // El tamaño de la lista
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        // El numero de columnass
       return columnas.length;
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        DetalleAsiento cuenta = lista.get(fila);
        switch (columna) {
            case 0:
                return cuenta.getId();
            case 1:
                return cuenta.getCuenta().getNombre();
            case 2:
                if (cuenta.getDebe() != 0.0){
                   return cuenta.getDebe(); 
                }else {
                   return cuenta.getHaber();
                }
                
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column]; 
    }
    
    public DetalleAsiento getDetalleAsientoByRow(int index){
        return lista.get(index);
    }
    
    
}
