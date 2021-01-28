package com.davidmcasas.simplenotepad;

import java.io.Serializable;

public class Categoria implements Serializable {

    private String nombre;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
