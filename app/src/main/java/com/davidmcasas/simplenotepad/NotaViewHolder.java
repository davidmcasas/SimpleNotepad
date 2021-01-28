package com.davidmcasas.simplenotepad;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class NotaViewHolder extends RecyclerView.ViewHolder {
    public TextView titulo, contenido;
    NotaViewHolder(View itemView) {
        super(itemView);
        titulo = itemView.findViewById(R.id.notaTitulo);
        contenido = itemView.findViewById(R.id.notaContenido);
    }
}
