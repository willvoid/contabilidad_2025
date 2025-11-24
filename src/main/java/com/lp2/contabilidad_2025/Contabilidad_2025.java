/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.lp2.contabilidad_2025;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.lp2.contabilidad_2025.controller.CuentaContableController;
import com.lp2.contabilidad_2025.controller.LibroDiarioController;
import com.lp2.contabilidad_2025.modelo.dao.AsientoCrudImpl;
import com.lp2.contabilidad_2025.modelo.dao.CuentaContableCrudImpl;
import com.lp2.contabilidad_2025.modelo.dao.DetalleAsientoCrudImpl;
import com.lp2.contabilidad_2025.vista.GUICuentaContable;
import com.lp2.contabilidad_2025.vista.GUILibroDiario;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicLookAndFeel;


public class Contabilidad_2025 {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        
        GUILibroDiario gui = new GUILibroDiario(null, true);
        AsientoCrudImpl crudAsiento = new AsientoCrudImpl();
        DetalleAsientoCrudImpl crudDetalle = new DetalleAsientoCrudImpl();
        LibroDiarioController ctrl = new LibroDiarioController(gui, crudAsiento, crudDetalle);
        ctrl.mostrarVentana();
        
//        CuentaContableCrudImpl crud = new CuentaContableCrudImpl();
//        GUICuentaContable gui = new GUICuentaContable(null, true);
//        
//        CuentaContableController ctrl = new CuentaContableController(gui, crud);
//        ctrl.mostrarVentana();
        
    }
}
