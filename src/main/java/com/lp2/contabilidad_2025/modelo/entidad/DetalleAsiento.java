/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.contabilidad_2025.modelo.entidad;

/**
 *
 * @author Windows 11
 */
public class DetalleAsiento {
    private Integer id;
    private CuentaContable cuenta;
    private Integer fkAsiento;
    private Double debe;
    private Double haber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CuentaContable getCuenta() {
        return cuenta;
    }

    public void setCuenta(CuentaContable cuenta) {
        this.cuenta = cuenta;
    }

    public Integer getFkAsiento() {
        return fkAsiento;
    }

    public void setFkAsiento(Integer fkAsiento) {
        this.fkAsiento = fkAsiento;
    }

    public Double getDebe() {
        return debe;
    }

    public void setDebe(Double debe) {
        this.debe = debe;
    }

    public Double getHaber() {
        return haber;
    }

    public void setHaber(Double haber) {
        this.haber = haber;
    }
    
    
    
}
