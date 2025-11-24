/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.contabilidad_2025.modelo.tabla;



import com.lp2.contabilidad_2025.modelo.entidad.Asiento;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class AsientoTablaModel extends AbstractTableModel{
    List<Asiento> lista;
    private String[] columnas ={"ID","FECHA","DESCRIPCION"};
    
    public void setLista(List<Asiento> lista){
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
        Asiento cuenta = lista.get(fila);
        switch (columna) {
            case 0:
                return cuenta.getId();
            case 1:
                return cuenta.getFecha();
            case 2:
                return cuenta.getDescripcion();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column]; 
    }
    
    public Asiento getAsientoByRow(int index){
        return lista.get(index);
    }
    
    
}
