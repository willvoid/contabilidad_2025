/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.contabilidad_2025.controller;

import com.lp2.contabilidad_2025.modelo.dao.CuentaContableCrudImpl;
import com.lp2.contabilidad_2025.modelo.dao.TipoCuentaCrudImpl;
import com.lp2.contabilidad_2025.modelo.entidad.CuentaContable;
import com.lp2.contabilidad_2025.modelo.entidad.TipoCuenta;
import com.lp2.contabilidad_2025.modelo.tabla.CuentaContableTablaModel;
import com.lp2.contabilidad_2025.vista.GUICuentaContable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author cmendieta
 */
public class CuentaContableController implements ActionListener , KeyListener {
    
    private GUICuentaContable gui;
    private CuentaContableCrudImpl crud;
    
    private char operacion;
    CuentaContable cuenta = new CuentaContable();
    
    CuentaContableTablaModel modelo = new CuentaContableTablaModel();
    
    public CuentaContableController(GUICuentaContable gui, CuentaContableCrudImpl crud) {
        this.gui = gui;
        this.crud = crud;
        this.gui.btn_guardar.addActionListener(this);
        this.gui.btn_cancelar.addActionListener(this);
        this.gui.btn_nuevo.addActionListener(this);
        this.gui.btn_editar.addActionListener(this);
        this.gui.btn_eliminar.addActionListener(this);
        this.gui.txt_buscar.addKeyListener(this);
        
        gui.tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTable tabla = (JTable) e.getSource();
                int row = tabla.rowAtPoint(e.getPoint());
                CuentaContableTablaModel model = (CuentaContableTablaModel) tabla.getModel();
                //Devolver el objeto seleccionado en la fila

                setCuentaContableForm(model.getCuentaContableByRow(row));
            }
        });
        
        llenarComboTipo(gui.cbo_tipo);
        habilitarCampos(false);
        habilitarBoton(false);
   
        listar("");
    }
    
    public void mostrarVentana() {
        gui.setLocationRelativeTo(gui);
        gui.setVisible(true);
    }
    
    public void listar(String valorBuscado) {
        List<CuentaContable> lista = crud.listar(valorBuscado);
        modelo.setLista(lista);
        gui.tabla.setModel(modelo);
        gui.tabla.updateUI();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Evento click");
        
        if(e.getSource()== gui.txt_buscar){
            String valor = gui.txt_buscar.getText();
            listar(valor);
        }
        if (e.getSource() == gui.btn_nuevo) {
            operacion = 'N';
            limpiar();
            habilitarCampos(true);
             habilitarBoton(true);
            gui.txt_nombre.requestFocus();
        }
        if (e.getSource() == gui.btn_editar) {
            operacion = 'E';
            habilitarCampos(true);
             habilitarBoton(true);
            gui.txt_nombre.requestFocus();
        }
        
        if (e.getSource() == gui.btn_eliminar) {
            int fila = gui.tabla.getSelectedRow();
            if (fila >= 0) {
                int ok = JOptionPane.showConfirmDialog(gui,
                        "Realmente desea elimnar el registro?", 
                        "Confirmar operacion", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);
                if (ok == 0) {
                    crud.eliminar(modelo.getCuentaContableByRow(fila));
                    listar("");
                }
            } else {
                JOptionPane.showMessageDialog(gui, "Debe seleccionar una fila");
            }
        }
        if (e.getSource() == gui.btn_cancelar) {
            habilitarCampos(false);
             habilitarBoton(false);
            limpiar();
        }
        
        if (e.getSource() == gui.btn_guardar) {
            boolean v_control = validarDatos();
            if(v_control == true){
                JOptionPane.showMessageDialog(gui, "Favor completar los datos");
                return;
            }
            
            Integer tipo_seleccionado = gui.cbo_tipo.getSelectedIndex();
            if (tipo_seleccionado == 0){
                JOptionPane.showMessageDialog(gui, "Seleccione un tipo de cuenta válido.");
                return;
            }
            
            System.out.println("Evento click de guardar");
            if (operacion == 'N') {
                crud.insertar(getCuentaContableForm());
                
                gui.txt_nombre.requestFocus();
            }
            
            if (operacion == 'E') {
                crud.actualizar(getCuentaContableForm());
                habilitarCampos(false);
            }
            
            listar("");
            limpiar();
            
        }
        System.out.println(operacion);
    }

    // Metodo encargado de habilitar o deshabilitar los campos
    private void habilitarCampos(Boolean estado) {
        gui.txt_nombre.setEnabled(estado);
        gui.txt_codigo.setEnabled(estado);
        gui.cbo_tipo.setEnabled(estado);
    }
    
      private void habilitarBoton(Boolean estado) {
        gui.btn_guardar.setEnabled(estado);
        gui.btn_cancelar.setEnabled(estado);
    }
    
    private void limpiar() {
        gui.txt_nombre.setText("");
        gui.txt_codigo.setText("");
        gui.cbo_tipo.setSelectedIndex(0);
    }

    // funcion o metodo encargado de recuperrar los valores de los JTextField en un objeto
    private CuentaContable getCuentaContableForm() {
        cuenta.setNombre(gui.txt_nombre.getText());
        cuenta.setCodigo(gui.txt_codigo.getText());
        cuenta.setTipo((TipoCuenta) gui.cbo_tipo.getSelectedItem());
        return cuenta;
    }

    //Funcion o metodo encargado asignar valor los JTextField
    private void setCuentaContableForm(CuentaContable item) {
        System.out.println(item);
        cuenta.setId(item.getId());
        gui.txt_nombre.setText(item.getNombre());
        gui.txt_codigo.setText(item.getCodigo());
        gui.cbo_tipo.setSelectedItem(item.getTipo());
    }
    
    private boolean validarDatos(){
        boolean vacio = false;
        if(gui.txt_nombre.getText().isEmpty()){
            vacio = true;
        }
        return vacio;
    }

    private void llenarComboTipo(JComboBox cbo){
        DefaultComboBoxModel<TipoCuenta> model = new DefaultComboBoxModel();
        TipoCuentaCrudImpl crudTipoCuenta = new TipoCuentaCrudImpl();
        // Agregar el item "Seleccionar Marca"
        TipoCuenta seleccionar = new TipoCuenta();
        // Nombre especial para distinguir
        seleccionar.setNombre("Seleccionar TipoCuenta");
        model.addElement(seleccionar);
        
        List<TipoCuenta> lista = crudTipoCuenta.listar("");
        for (int i = 0; i < lista.size(); i++) {
            TipoCuenta iva = lista.get(i);
            model.addElement(iva);
        }
        cbo.setModel(model);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
      
    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.listar(gui.txt_buscar.getText());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
