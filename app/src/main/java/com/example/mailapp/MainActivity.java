package com.example.mailapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText editarCorreoOrigen, editarCorreoDestino, editarAsunto, editarCuerpoCorreo;
    private Button botonAceptar, botonCancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicialización de los componentes
        editarCorreoOrigen  = findViewById(R.id.editarCorreoOrigen);
        editarCorreoDestino = findViewById(R.id.editarCorreoDestino);
        editarAsunto        = findViewById(R.id.editarAsunto);
        editarCuerpoCorreo  = findViewById(R.id.editarCuerpoCorreo);
        botonAceptar        = findViewById(R.id.botonAceptar);
        botonCancelar       = findViewById(R.id.botonCancelar);

        //Recuperar los datos de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("EmailApp",MODE_PRIVATE);
        String correoOrigen                 = sharedPreferences.getString("correoOrigen","");
        String correoDestino                = sharedPreferences.getString("correoDestino","");
        String asunto                       = sharedPreferences.getString("asunto","");
        String mensaje                      = sharedPreferences.getString("mensaje","");

        //configurar los valores en los editText
        editarCorreoOrigen.setText(correoOrigen);
        editarCorreoDestino.setText(correoDestino);
        editarAsunto.setText(asunto);
        editarCuerpoCorreo.setText(mensaje);

        //Configuración del botón "Aceptar"
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar los datos en shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("correoOrigen",editarCorreoOrigen.getText().toString());
                editor.putString("correoDestino",editarCorreoDestino.getText().toString());
                editor.putString("asunto",editarAsunto.getText().toString());
                editor.putString("mensaje",editarCuerpoCorreo.getText().toString());
                editor.apply();

                //llamar al método para enviar el correo
                enviarCorreo();
            }
        });

        //botón cancelar
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //limpia los campos de texto
                editarCorreoOrigen.setText("");
                editarCorreoDestino.setText("");
                editarAsunto.setText("");
                editarCuerpoCorreo.setText("");

                //Limpiar los datos de shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
            }
        });
    }
    private void enviarCorreo(){
        String correoDestino = editarCorreoDestino.getText().toString();
        String asunto        = editarAsunto.getText().toString();
        String mensaje       = editarCuerpoCorreo.getText().toString();

        //intent implícito
        Intent intentCorreo = new Intent(Intent.ACTION_SEND);
        intentCorreo.setType("message/rfc22");
        intentCorreo.putExtra(Intent.EXTRA_EMAIL,new String[]{correoDestino});
        intentCorreo.putExtra(Intent.EXTRA_SUBJECT, asunto);
        intentCorreo.putExtra(Intent.EXTRA_TEXT, mensaje);

        try {
            startActivity(Intent.createChooser(intentCorreo,"Enviar correo"));
        }catch (android.content.ActivityNotFoundException exception){
            Toast.makeText(MainActivity.this,"No jaló",Toast.LENGTH_LONG).show();
        }
    }
}