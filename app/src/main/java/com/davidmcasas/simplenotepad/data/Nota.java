package com.davidmcasas.simplenotepad.data;

public class Nota {

    private Categoria categoria;
    private String titulo;
    private String contenido;

    public Nota() {
        this.categoria = null;
        this.titulo = "";
        this.contenido = "";
    }

    public Nota(String titulo, String contenido) {
        this.categoria = null;
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public Nota(String titulo, String contenido, Categoria categoria) {
        this.categoria = categoria;
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
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

}
