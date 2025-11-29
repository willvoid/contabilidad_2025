package com.lp2.contabilidad_2025.modelo.tabla;

import com.lp2.contabilidad_2025.modelo.entidad.DetalleAsiento;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import com.lp2.contabilidad_2025.modelo.entidad.DetalleAsiento;

import com.lp2.contabilidad_2025.modelo.entidad.DetalleAsiento;

import com.lp2.contabilidad_2025.modelo.entidad.DetalleAsiento;

public class DetalleAsientoTablaModel extends AbstractTableModel {
    
    private List<DetalleAsiento> lista;
    private String[] columnas = {"CUENTA", "VALOR"};
    
    // Constructor inicializa la lista vacía
    public DetalleAsientoTablaModel() {
        this.lista = new ArrayList<>();
    }
    
    public void setLista(List<DetalleAsiento> lista) {
        // Inicializamos la lista de detalles
        this.lista = (lista != null) ? lista : new ArrayList<>();
        fireTableDataChanged(); // Notificar que los datos cambiaron
    }
    
    @Override
    public int getRowCount() {
        // El tamaño de la lista
        return lista.size();
    }
    
    @Override
    public int getColumnCount() {
        // El número de columnas
        return columnas.length;
    }
    
    @Override
    public Object getValueAt(int fila, int columna) {
        if (fila < 0 || fila >= lista.size()) {
            return null;
        }
        
        DetalleAsiento detalle = lista.get(fila);
        
        switch (columna) {
            case 0:
                return (detalle.getCuenta() != null) 
                       ? detalle.getCuenta().getNombre() 
                       : "";
                
            case 1:
                // Retorna el valor que no sea cero
                if (detalle.getDebe() != null && detalle.getDebe() != 0.0) {
                    return String.format("%.2f", detalle.getDebe());
                } else if (detalle.getHaber() != null && detalle.getHaber() != 0.0) {
                    return String.format("%.2f", detalle.getHaber());
                }
                return "0.00";
                
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        if (column >= 0 && column < columnas.length) {
            return columnas[column];
        }
        return "";
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            default:
                return Object.class;
        }
    }
    
    public DetalleAsiento getDetalleAsientoByRow(int index) {
        if (index >= 0 && index < lista.size()) {
            return lista.get(index);
        }
        return null;
    }
    
    // Método adicional para saber si una fila es debe o haber
    public boolean esDebe(int fila) {
        if (fila >= 0 && fila < lista.size()) {
            DetalleAsiento detalle = lista.get(fila);
            return detalle.getDebe() != null && detalle.getDebe() != 0.0;
        }
        return false;
    }
    
    public boolean esHaber(int fila) {
        if (fila >= 0 && fila < lista.size()) {
            DetalleAsiento detalle = lista.get(fila);
            return detalle.getHaber() != null && detalle.getHaber() != 0.0;
        }
        return false;
    }
    
    // Método para limpiar la tabla
    public void limpiar() {
        this.lista.clear();
        fireTableDataChanged();
    }
}