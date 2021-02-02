package com.davidmcasas.simplenotepad.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Nota {

    private Categoria categoria;
    private String titulo;
    private String contenido;
    private String fecha;

    public Nota() {
        this.categoria = null;
        this.titulo = "";
        this.contenido = "";
        updateFecha();
    }

    public Nota(String titulo, String contenido) {
        this.categoria = null;
        this.titulo = titulo;
        this.contenido = contenido;
        updateFecha();
    }

    public Nota(String titulo, String contenido, Categoria categoria) {
        this.categoria = categoria;
        this.titulo = titulo;
        this.contenido = contenido;
        updateFecha();
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        updateFecha();
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
        updateFecha();
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
        updateFecha();
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    private void updateFecha() {
        this.fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public String getFecha() {
        return fecha;
    }
}
