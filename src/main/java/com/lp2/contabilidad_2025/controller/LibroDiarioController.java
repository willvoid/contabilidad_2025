/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.contabilidad_2025.controller;

import com.formdev.flatlaf.json.ParseException;
import com.lp2.contabilidad_2025.modelo.dao.AsientoCrudImpl;
import com.lp2.contabilidad_2025.modelo.dao.CuentaContableCrudImpl;
import com.lp2.contabilidad_2025.modelo.dao.DetalleAsientoCrudImpl;
import com.lp2.contabilidad_2025.modelo.dao.CuentaContableCrudImpl;
import com.lp2.contabilidad_2025.modelo.entidad.Asiento;
import com.lp2.contabilidad_2025.modelo.entidad.CuentaContable;
import com.lp2.contabilidad_2025.modelo.entidad.CuentaContable;
import com.lp2.contabilidad_2025.modelo.entidad.DetalleAsiento;
import com.lp2.contabilidad_2025.modelo.tabla.CuentaContableTablaModel;
import com.lp2.contabilidad_2025.modelo.tabla.DetalleAsientoTablaModel;
import com.lp2.contabilidad_2025.vista.GUICuentaContable;
import com.lp2.contabilidad_2025.vista.GUILibroDiario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;


public class LibroDiarioController implements ActionListener , KeyListener {
    
    private GUILibroDiario gui;
    private AsientoCrudImpl crudAsiento;
    private DetalleAsientoCrudImpl crudDetalle;
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private char operacion;
    Asiento asiento = new Asiento();
    DetalleAsiento detalle = new DetalleAsiento();
    
    DetalleAsientoTablaModel modelo = new DetalleAsientoTablaModel();
    
    public LibroDiarioController(GUILibroDiario gui, AsientoCrudImpl crudAsiento, DetalleAsientoCrudImpl crudDetalle) {
        this.gui = gui;
        this.crudAsiento = crudAsiento;
        this.crudDetalle = crudDetalle;
        
        this.gui.btn_guardar.addActionListener(this);
        this.gui.btn_cancelar.addActionListener(this);
        this.gui.btn_nuevo.addActionListener(this);
        this.gui.btn_editar.addActionListener(this);
        this.gui.btn_eliminar_detalle.addActionListener(this);
        this.gui.btn_insertar.addActionListener(this);
        
        //this.gui.txt_buscar.addKeyListener(this);
        
        gui.tabla_debe.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTable tabla = (JTable) e.getSource();
                int row = tabla.rowAtPoint(e.getPoint());
                DetalleAsientoTablaModel model = (DetalleAsientoTablaModel) tabla.getModel();
                //Devolver el objeto seleccionado en la fila

                setDebeForm(model.getDetalleAsientoByRow(row));
            }
        });
        
        llenarComboCuenta(gui.cbo_cuenta1);
        habilitarCampos(false);
        habilitarBoton(false);
        obtenerIdAsiento();
        leerAsiento(gui.txt_idasiento.getText());
        Integer valorSecuencia = crudDetalle.obtenerValorActualSecuencia();
        listar(valorSecuencia.toString());
    }
    
    private void obtenerIdAsiento(){
        Integer valorActual = crudDetalle.obtenerValorActualSecuencia();
        gui.txt_idasiento.setText(valorActual.toString());
        /*if (valorActual == 1){
            gui.txt_idasiento.setText(valorActual.toString());
        }else{
            Integer valor = valorActual + 1;
            gui.txt_idasiento.setText(valor.toString());
        }*/
    }
    
    public void mostrarVentana() {
        gui.setLocationRelativeTo(gui);
        gui.setVisible(true);
    }
    
    public void listar(String valorBuscado) {
        List<DetalleAsiento> lista = crudDetalle.listar(valorBuscado);
        modelo.setLista(lista);
        gui.tabla_debe.setModel(modelo);
        gui.tabla_debe.updateUI();
    }
    
    public void leerAsiento (String idAsiento){
        Asiento asiento = crudAsiento.leerAsiento(idAsiento);
        gui.txt_descripcion.setText(asiento.getDescripcion());
        gui.txt_fecha.setText(asiento.getFecha().toString());
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Evento click");
        
        /*if(e.getSource()== gui.txt_buscar){
            String valor = gui.txt_buscar.getText();
            listar(valor);
        }*/
        if (e.getSource() == gui.btn_nuevo) {
            operacion = 'N';
            limpiar();
            habilitarCampos(true);
            habilitarBoton(true);
            gui.txt_descripcion.requestFocus();
        }
        if (e.getSource() == gui.btn_editar) {
            operacion = 'E';
            habilitarCampos(true);
             habilitarBoton(true);
            gui.txt_descripcion.requestFocus();
        }
        
        if (e.getSource() == gui.btn_eliminar_detalle) {
            int fila = gui.tabla_debe.getSelectedRow();
            if (fila >= 0) {
                int ok = JOptionPane.showConfirmDialog(gui,
                        "Realmente desea elimnar el registro?", 
                        "Confirmar operacion", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);
                if (ok == 0) {
                    crudDetalle.eliminar(modelo.getDetalleAsientoByRow(fila));
                    listar(gui.txt_idasiento.getText());
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
        
        if (e.getSource() == gui.btn_insertar) {
            boolean v_control = validarDatos();
            if(v_control == true){
                JOptionPane.showMessageDialog(gui, "Favor completar los datos");
                return;
            }
            
            Integer tipo_seleccionado = gui.cbo_cuenta1.getSelectedIndex();
            if (tipo_seleccionado == 0){
                JOptionPane.showMessageDialog(gui, "Seleccione una cuenta válida.");
                return;
            }
            
            System.out.println("Evento click de guardar");
            if (operacion == 'N') {
                crudDetalle.insertar(getDebeForm());
                
                gui.cbo_cuenta1.requestFocus();
            }
            
            if (operacion == 'E') {
                crudDetalle.actualizar(getDebeForm());
                habilitarCampos(false);
            }
            
            listar(gui.txt_idasiento.getText());
            limpiar();
            
        }
        
        if (e.getSource() == gui.btn_guardar) {
            boolean v_control = validarDatosAsiento();
            if(v_control == true){
                JOptionPane.showMessageDialog(gui, "Favor completar los datos");
                return;
            }
            
            System.out.println("Evento click de guardar");
            if (operacion == 'N') {
                try {
                    crudAsiento.insertar(getAsientoForm());
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(LibroDiarioController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                gui.cbo_cuenta1.requestFocus();
            }
            
            if (operacion == 'E') {
                try {
                    crudAsiento.actualizar(getAsientoForm());
                    habilitarCampos(false);
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(LibroDiarioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            //listar("");
            //limpiar();
            
        }
        
        System.out.println(operacion);
    }

    // Metodo encargado de habilitar o deshabilitar los campos
    private void habilitarCampos(Boolean estado) {
        gui.txt_descripcion.setEnabled(estado);
        gui.txt_valordebe.setEnabled(estado);
        gui.cbo_cuenta1.setEnabled(estado);
    }
    
      private void habilitarBoton(Boolean estado) {
        gui.btn_guardar.setEnabled(estado);
        gui.btn_cancelar.setEnabled(estado);
    }
    
    private void limpiar() {
        gui.txt_valordebe.setText("");
        gui.txt_descripcion.setText("");
        gui.cbo_cuenta1.setSelectedIndex(0);
    }

    // funcion o metodo encargado de recuperrar los valores de los JTextField en un objeto
    private DetalleAsiento getDebeForm() {
        detalle.setDebe(Double.valueOf(gui.txt_valordebe.getText()));
        detalle.setCuenta((CuentaContable) gui.cbo_cuenta1.getSelectedItem());
        detalle.setFkAsiento(Integer.valueOf(gui.txt_idasiento.getText()));
        detalle.setHaber(0.0);
        return detalle;
    }
    
    private Asiento getAsientoForm() throws java.text.ParseException {
        try {
            Date fecha = sdf.parse(gui.txt_fecha.getText());
            asiento.setFecha(new java.sql.Date(fecha.getTime()));
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Usa el formato dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        asiento.setDescripcion(gui.txt_descripcion.getText());
        return asiento;
    }


    //Funcion o metodo encargado asignar valor los JTextField
    private void setDebeForm(DetalleAsiento item) {
        System.out.println(item);
        asiento.setId(item.getId());
        gui.txt_valordebe.setText(item.getDebe().toString());
        gui.cbo_cuenta1.setSelectedItem(item.getCuenta());
    }
    
    private boolean validarDatos(){
        boolean vacio = false;
        if(gui.txt_valordebe.getText().isEmpty()){
            vacio = true;
        }
        return vacio;
    }
    
    private boolean validarDatosAsiento(){
        boolean vacio = false;
        if(gui.txt_fecha.getText().isEmpty()){
            vacio = true;
        }
        if(gui.txt_descripcion.getText().isEmpty()){
            vacio = true;
        }
        return vacio;
    }

    private void llenarComboCuenta(JComboBox cbo){
        DefaultComboBoxModel<CuentaContable> model = new DefaultComboBoxModel();
        CuentaContableCrudImpl crudCuentaContable = new CuentaContableCrudImpl();
        // Agregar el item "Seleccionar Marca"
        CuentaContable seleccionar = new CuentaContable();
        // Nombre especial para distinguir
        seleccionar.setNombre("Seleccionar CuentaContable");
        model.addElement(seleccionar);
        
        List<CuentaContable> lista = crudCuentaContable.listar("");
        for (int i = 0; i < lista.size(); i++) {
            CuentaContable iva = lista.get(i);
            model.addElement(iva);
        }
        cbo.setModel(model);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
      
    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.listar(gui.txt_idasiento.getText());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
