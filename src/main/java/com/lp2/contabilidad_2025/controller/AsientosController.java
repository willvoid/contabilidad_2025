package com.lp2.contabilidad_2025.controller;

import com.lp2.contabilidad_2025.modelo.dao.AsientoCrudImpl;
import com.lp2.contabilidad_2025.modelo.dao.DetalleAsientoCrudImpl;
import com.lp2.contabilidad_2025.modelo.dao.TipoCuentaCrudImpl;
import com.lp2.contabilidad_2025.modelo.entidad.Asiento;
import com.lp2.contabilidad_2025.modelo.entidad.TipoCuenta;
import com.lp2.contabilidad_2025.modelo.tabla.AsientoTablaModel;
import com.lp2.contabilidad_2025.vista.GUICuentaAsientos;
import com.lp2.contabilidad_2025.vista.GUILibroDiario;
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


public class AsientosController implements ActionListener , KeyListener {
    
    private GUICuentaAsientos gui;
    private AsientoCrudImpl crud;
    
    private char operacion;
    
    AsientoTablaModel modelo = new AsientoTablaModel();
    
    public AsientosController(GUICuentaAsientos gui, AsientoCrudImpl crud) {
        this.gui = gui;
        this.crud = crud;
        this.gui.btn_nuevo.addActionListener(this);
        //this.gui.btn_editar.addActionListener(this);
        //this.gui.btn_eliminar.addActionListener(this);
        this.gui.txt_buscar.addKeyListener(this);
        
        // Listener para doble clic en la tabla
        gui.tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable tabla = (JTable) e.getSource();
                int row = tabla.rowAtPoint(e.getPoint());
                AsientoTablaModel model = (AsientoTablaModel) tabla.getModel();
                
                // Detectar doble clic
                if (e.getClickCount() == 2 && row >= 0) {
                    // Obtener el asiento seleccionado
                    Asiento asientoSeleccionado = model.getAsientoByRow(row);
                    
                    if (asientoSeleccionado != null) {
                        abrirLibroDiario(asientoSeleccionado.getId());
                    }
                }
            }
        });
        
        listar("");
    }
    
    /**
     * Método para abrir el Libro Diario con un ID de asiento específico
     * @param idAsiento El ID del asiento a cargar
     */
    private void abrirLibroDiario(int idAsiento) {
        try {
            // Crear la ventana y los objetos necesarios
            GUILibroDiario guiLibro = new GUILibroDiario(null, true);
            AsientoCrudImpl crudAsiento = new AsientoCrudImpl();
            DetalleAsientoCrudImpl crudDetalle = new DetalleAsientoCrudImpl();
            LibroDiarioController ctrl = new LibroDiarioController(guiLibro, crudAsiento, crudDetalle);
            
            // Setear el ID del asiento en el campo de texto
            guiLibro.txt_idasiento.setText(String.valueOf(idAsiento));
            
            // Cargar los datos del asiento
            ctrl.leerAsiento(String.valueOf(idAsiento));
            
            // Listar los detalles (debe y haber)
            ctrl.listar(String.valueOf(idAsiento));
            ctrl.listarHaber(String.valueOf(idAsiento));
            
            // Mostrar la ventana
            ctrl.mostrarVentana();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(gui, 
                "Error al abrir el Libro Diario: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    public void mostrarVentana() {
        gui.setLocationRelativeTo(gui);
        gui.setVisible(true);
    }
    
    public void listar(String valorBuscado) {
        List<Asiento> lista = crud.listar(valorBuscado);
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
            abrirLibroDiario(0);
        }
//        if (e.getSource() == gui.btn_editar) {
//            operacion = 'E';
//            // Obtener la fila seleccionada
//            int filaSeleccionada = gui.tabla.getSelectedRow();
//            if (filaSeleccionada >= 0) {
//                Asiento asientoSeleccionado = modelo.getAsientoByRow(filaSeleccionada);
//                if (asientoSeleccionado != null) {
//                    abrirLibroDiario(asientoSeleccionado.getId());
//                }
//            } else {
//                JOptionPane.showMessageDialog(gui, "Debe seleccionar un asiento para editar");
//            }
//        }
        
        System.out.println(operacion);
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