package com.davidmcasas.simplenotepad.adapters;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.davidmcasas.simplenotepad.R;

public class NotaViewHolder extends RecyclerView.ViewHolder {
    public TextView titulo, contenido, fecha;
    public NotaViewHolder(View itemView) {
        super(itemView);
        titulo = itemView.findViewById(R.id.notaTitulo);
        contenido = itemView.findViewById(R.id.notaContenido);
        fecha = itemView.findViewById(R.id.notaFecha);
    }
}
