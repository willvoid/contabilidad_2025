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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


public class LibroDiarioController implements ActionListener , KeyListener {
    
    private GUILibroDiario gui;
    private AsientoCrudImpl crudAsiento;
    private DetalleAsientoCrudImpl crudDetalle;
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private char operacion;
    
    private char operacionDetalle;
    Asiento asiento = new Asiento();
    DetalleAsiento detalle = new DetalleAsiento();
    
    // MODELOS SEPARADOS PARA DEBE Y HABER
    DetalleAsientoTablaModel modeloDebe = new DetalleAsientoTablaModel();
    DetalleAsientoTablaModel modeloHaber = new DetalleAsientoTablaModel();
    
    public LibroDiarioController(GUILibroDiario gui, AsientoCrudImpl crudAsiento, DetalleAsientoCrudImpl crudDetalle) {
        this.gui = gui;
        this.crudAsiento = crudAsiento;
        this.crudDetalle = crudDetalle;
        
        this.gui.btn_guardar.addActionListener(this);
        this.gui.btn_cancelar.addActionListener(this);
        this.gui.btn_nuevo.addActionListener(this);
        this.gui.btn_editar.addActionListener(this);
        this.gui.btn_eliminar_detalle.addActionListener(this);
        this.gui.btn_eliminar_detalleHaber.addActionListener(this);
        this.gui.btn_insertar.addActionListener(this);
        this.gui.btn_insertarHaber.addActionListener(this);
         this.gui.cbo_cuenta1.addActionListener(this);
         //gui.txt_valordebe.setFocusable(true);
        
        //this.gui.txt_buscar.addKeyListener(this);
        
        gui.tabla_debe.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTable tabla = (JTable) e.getSource();
                int row = tabla.rowAtPoint(e.getPoint());
                DetalleAsientoTablaModel model = (DetalleAsientoTablaModel) tabla.getModel();
                DetalleAsiento detalleSeleccionado = model.getDetalleAsientoByRow(row);
                if (detalleSeleccionado != null) {
                    setDebeForm(detalleSeleccionado);
                }
            }
        });
        
        // Enter ejecuta guardar
        this.gui.txt_descripcion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gui.btn_guardar.doClick();
                    e.consume(); // Evita que se agregue un salto de línea
                }
            }
        });
        
        gui.tabla_debe1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTable tabla = (JTable) e.getSource();
                int row = tabla.rowAtPoint(e.getPoint());
                DetalleAsientoTablaModel model = (DetalleAsientoTablaModel) tabla.getModel();
                DetalleAsiento detalleSeleccionado = model.getDetalleAsientoByRow(row);
                if (detalleSeleccionado != null) {
                    setHaberForm(detalleSeleccionado);
                }
            }
        });
        
        
        // Enter ejecuta guardar
        this.gui.txt_valordebe.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gui.btn_insertar.doClick();
                    e.consume(); // Evita que se agregue un salto de línea
                }
            }
        });
        
        // Enter ejecuta guardar
        this.gui.txt_valorHaber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gui.btn_insertarHaber.doClick();
                    e.consume(); // Evita que se agregue un salto de línea
                }
            }
        });
        
        
       // Obtener el componente editor del combo y agregar el listener
        JComponent editor = (JComponent) gui.cbo_cuenta1.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (gui.cbo_cuenta1.getSelectedIndex() > 0) {
                        gui.txt_valordebe.requestFocus();
                        gui.txt_valordebe.selectAll();
                    }
                }
            }
        });
        
        // Obtener el componente editor del combo y agregar el listener para cbo_cuenta2
        JComponent editor2 = (JComponent) gui.cbo_cuenta2.getEditor().getEditorComponent();
        editor2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (gui.cbo_cuenta2.getSelectedIndex() > 0) {
                        gui.txt_valorHaber.requestFocus();
                        gui.txt_valorHaber.selectAll();
                    }
                }
            }
        });
        
        


        operacionDetalle = 'N';
        
        habilitarBotonDetalle(false);
        habilitarCamposDetalle(false);
        gui.txt_fecha.setEnabled(false);
        llenarComboCuenta(gui.cbo_cuenta1);
        llenarComboCuenta(gui.cbo_cuenta2);
        habilitarCampos(false);
        habilitarBoton(false);
        obtenerIdAsiento();
        leerAsiento(gui.txt_idasiento.getText());
        Integer valorSecuencia = crudDetalle.obtenerValorActualSecuencia();
        listar(valorSecuencia.toString());
        listarHaber(valorSecuencia.toString());
        gui.txt_idasiento.setVisible(false);
        gui.txt_idasiento.setEnabled(false);
        gui.btn_eliminar.setVisible(false);
        gui.btn_eliminar.setEnabled(false);
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
        // Filtrar solo los que tienen DEBE
        List<DetalleAsiento> listaDebe = new ArrayList<>();
        for (DetalleAsiento detalle : lista) {
            if (detalle.getDebe() != null && detalle.getDebe() != 0.0) {
                listaDebe.add(detalle);
            }
        }
        modeloDebe.setLista(listaDebe);
        gui.tabla_debe.setModel(modeloDebe);
        gui.tabla_debe.updateUI();
    }
    
     public void listarHaber(String valorBuscado) {
        List<DetalleAsiento> lista = crudDetalle.listar(valorBuscado);
        // Filtrar solo los que tienen HABER
        List<DetalleAsiento> listaHaber = new ArrayList<>();
        for (DetalleAsiento detalle : lista) {
            if (detalle.getHaber() != null && detalle.getHaber() != 0.0) {
                listaHaber.add(detalle);
            }
        }
        modeloHaber.setLista(listaHaber);
        gui.tabla_debe1.setModel(modeloHaber);
        gui.tabla_debe1.updateUI();
    }
    
    public void leerAsiento (String idAsiento){
        Asiento asiento = crudAsiento.leerAsiento(idAsiento);
        if (asiento == null){
            habilitarBoton(true);
            habilitarCampos(true);
            operacion = 'N';
            establecerFechaActual();
            gui.txt_descripcion.setText("");
            gui.txt_descripcion.requestFocus();
            return;
        }
        gui.txt_descripcion.setText(asiento.getDescripcion());
        if (asiento.getFecha() != null) {
            java.util.Date fecha = new java.util.Date(asiento.getFecha().getTime()); // Convertir de java.sql.Date a java.util.Date
            gui.txt_fecha.setText(sdf.format(fecha)); // Usa el formato deseado
        } else {
            gui.txt_fecha.setText(""); // Limpia el campo si no hay fecha
        }
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
            listar("0");
            listarHaber("0");
            establecerFechaActual();
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
                    crudDetalle.eliminar(modeloDebe.getDetalleAsientoByRow(fila));
                    listar(gui.txt_idasiento.getText());
                    limpiarDetalle();
                }
            } else {
                JOptionPane.showMessageDialog(gui, "Debe seleccionar una fila");
            }
        }
        
        if (e.getSource() == gui.btn_eliminar_detalleHaber) {
            int fila = gui.tabla_debe1.getSelectedRow();
            if (fila >= 0) {
                int ok = JOptionPane.showConfirmDialog(gui,
                        "Realmente desea elimnar el registro?", 
                        "Confirmar operacion", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);
                if (ok == 0) {
                    crudDetalle.eliminar(modeloHaber.getDetalleAsientoByRow(fila));
                    listarHaber(gui.txt_idasiento.getText());
                    limpiarDetalleHaber();
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
            if (operacionDetalle == 'N') {
                crudDetalle.insertar(getDebeForm());
                
                gui.cbo_cuenta1.requestFocus();
            }
            
            if (operacionDetalle == 'E') {
                crudDetalle.actualizar(getDebeForm());
                habilitarCampos(false);
            }
            //obtenerIdAsiento();
            listar(gui.txt_idasiento.getText());
            limpiarDetalle();
            
        }
        
        if (e.getSource() == gui.btn_insertarHaber) {
            boolean v_control = validarDatosHaber();
            if(v_control == true){
                JOptionPane.showMessageDialog(gui, "Favor completar los datos");
                return;
            }
            
            Integer tipo_seleccionado = gui.cbo_cuenta2.getSelectedIndex();
            if (tipo_seleccionado == 0){
                JOptionPane.showMessageDialog(gui, "Seleccione una cuenta válida.");
                return;
            }
            
            System.out.println("Evento click de guardar");
            if (operacionDetalle == 'N') {
                crudDetalle.insertar(getHaberForm());
                
                gui.cbo_cuenta1.requestFocus();
            }
            
            if (operacionDetalle == 'E') {
                crudDetalle.actualizar(getHaberForm());
                habilitarCampos(false);
            }
            //obtenerIdAsiento();
            listarHaber(gui.txt_idasiento.getText());
            limpiarDetalleHaber();
            
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
                    habilitarCampos(false);
                    obtenerIdAsiento();
                    listar(gui.txt_idasiento.getText());
                    listarHaber(gui.txt_idasiento.getText());
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
            
            
            habilitarBotonDetalle(true);
            habilitarCamposDetalle(true);
            gui.cbo_cuenta1.requestFocus();
            habilitarBoton(false);
            habilitarCampos(false);
            //listar("");
            //limpiar();
            
        }
        
        System.out.println(operacion);
    }

    // Metodo encargado de habilitar o deshabilitar los campos
    private void habilitarCampos(Boolean estado) {
        gui.txt_descripcion.setEnabled(estado);
    }
    
    private void habilitarBoton(Boolean estado) {
        gui.btn_calendario.setEnabled(estado);
        gui.btn_guardar.setEnabled(estado);
        gui.btn_cancelar.setEnabled(estado);
    }
    
    private void habilitarBotonDetalle(Boolean estado) {
        gui.btn_insertar.setEnabled(estado);
        gui.btn_insertarHaber.setEnabled(estado);
        
        gui.btn_eliminar_detalle.setEnabled(estado);
        gui.btn_eliminar_detalleHaber.setEnabled(estado);
    }
    
    private void habilitarCamposDetalle(Boolean estado) {
        gui.txt_valorHaber.setEnabled(estado);
        gui.txt_valordebe.setEnabled(estado);
        gui.cbo_cuenta1.setEnabled(estado);
        gui.cbo_cuenta2.setEnabled(estado);
    }
    
    private void limpiar() {
        gui.txt_descripcion.setText("");
        //gui.txt_fecha.setText("");
    }
    
    private void limpiarDetalle() {
        gui.txt_valordebe.setText("");
        gui.cbo_cuenta1.setSelectedIndex(0);
    }
    
    private void limpiarDetalleHaber() {
        gui.txt_valorHaber.setText("");
        gui.cbo_cuenta2.setSelectedIndex(0);
    }

    // funcion o metodo encargado de recuperrar los valores de los JTextField en un objeto
    private DetalleAsiento getDebeForm() {
        detalle.setDebe(Double.valueOf(gui.txt_valordebe.getText()));
        detalle.setCuenta((CuentaContable) gui.cbo_cuenta1.getSelectedItem());
        detalle.setFkAsiento(Integer.valueOf(gui.txt_idasiento.getText()));
        detalle.setHaber(0.0);
        return detalle;
    }
    
    private DetalleAsiento getHaberForm() {
        detalle.setDebe(0.0);
        detalle.setCuenta((CuentaContable) gui.cbo_cuenta2.getSelectedItem());
        detalle.setFkAsiento(Integer.valueOf(gui.txt_idasiento.getText()));
        detalle.setHaber(Double.valueOf(gui.txt_valorHaber.getText()));
        return detalle;
    }
    
    private Asiento getAsientoForm() throws java.text.ParseException {
        asiento.setId(Integer.valueOf(gui.txt_idasiento.getText()));
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
    
    private void setHaberForm(DetalleAsiento item) {
        System.out.println(item);
        asiento.setId(item.getId());
        gui.txt_valorHaber.setText(item.getHaber().toString());
        gui.cbo_cuenta2.setSelectedItem(item.getCuenta());
    }
    
    private boolean validarDatos(){
        boolean vacio = false;
        if(gui.txt_valordebe.getText().isEmpty()){
            vacio = true;
        }
        return vacio;
    }
    
    private boolean validarDatosHaber(){
        boolean vacio = false;
        if(gui.txt_valorHaber.getText().isEmpty()){
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
    
    private void establecerFechaActual() {
        Date fechaActual = new Date();
        gui.txt_fecha.setText(sdf.format(fechaActual));
    }

    private void llenarComboCuenta(JComboBox cbo){
        DefaultComboBoxModel<CuentaContable> model = new DefaultComboBoxModel();
        CuentaContableCrudImpl crudCuentaContable = new CuentaContableCrudImpl();
        // Agregar el item "Seleccionar Marca"
        CuentaContable seleccionar = new CuentaContable();
        // Nombre especial para distinguir
        seleccionar.setNombre("Seleccionar CuentaContable");
        model.addElement(seleccionar);
        
        AutoCompleteDecorator.decorate(cbo);
        
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
