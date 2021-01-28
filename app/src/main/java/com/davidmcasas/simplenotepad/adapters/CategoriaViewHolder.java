package com.davidmcasas.simplenotepad.adapters;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.davidmcasas.simplenotepad.R;

public class CategoriaViewHolder extends RecyclerView.ViewHolder{
    public TextView nombre;
    public CategoriaViewHolder(View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.categoriaNombre);
    }
}
