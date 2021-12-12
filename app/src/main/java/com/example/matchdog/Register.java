package com.example.matchdog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.matchdog.database.UsuarioRoom;
import com.example.matchdog.database.dao.UsuarioDao;
import com.example.matchdog.models.Usuario;

public class Register extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextName ;
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);

        iniciarBancoUsuario();
    }

    public void register(View view) {
        Toast.makeText(this, "Registrando", Toast.LENGTH_SHORT).show();

        Usuario usuario = new Usuario();
        usuario.setName(editTextName.getText().toString());
        usuario.setPassword(editTextPassword.getText().toString());
        usuario.setEmail(editTextEmail.getText().toString());

        usuarioDao.salvar(usuario);

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void toBackInitial(View view) {
        Intent intent = new Intent(this, InitialActivity.class);
        startActivity(intent);
    }

    public void iniciarBancoUsuario(){
        usuarioDao = Room.databaseBuilder(getApplicationContext(), UsuarioRoom.class, "usuario.db")
                .allowMainThreadQueries()
                .build()
                .getRoomUsuarioDao();
    }
}