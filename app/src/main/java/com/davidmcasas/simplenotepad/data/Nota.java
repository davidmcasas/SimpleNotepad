package com.davidmcasas.simplenotepad.data;

public class Nota {

    private Categoria categoria;
    private String titulo;
    private String contenido;
    /*private long id;
    private static long id_count = 1;*/

    public Nota() {
        this.categoria = null;
        this.titulo = "";
        this.contenido = "";
        /*this.id = id_count++;*/
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

    /*public long getId() {return id;}
    public static long getId_count() { return id_count;}
    public void setId(long id) {this.id = id; }
    public static void setId_count(long id_count) {Nota.id_count = id_count;}*/
}
