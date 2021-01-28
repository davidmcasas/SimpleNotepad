package com.davidmcasas.simplenotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NeodatisHelper.getInstance(this).borrarCategoria(new Categoria("hola"));
    }
}