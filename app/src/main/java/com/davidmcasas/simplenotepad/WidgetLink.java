package com.davidmcasas.simplenotepad;

import com.davidmcasas.simplenotepad.data.Nota;

/**
 * Clase usada para asociar un widget a partir de su ID a un objeto de la base de datos,
 * en este caso un objeto Nota
 */
public class WidgetLink {

    public int id;
    public Nota nota;

    public WidgetLink(int id, Nota nota) {
        this.id = id;
        this.nota = nota;
    }
}
