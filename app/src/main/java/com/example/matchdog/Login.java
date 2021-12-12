package com.example.matchdog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.example.matchdog.database.UsuarioRoom;
import com.example.matchdog.database.dao.UsuarioDao;
import com.example.matchdog.databinding.ActivityMainBinding;
import com.example.matchdog.models.Usuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class Login extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private EditText editTextEmail, editTextPassword ;
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        iniciarBancoUsuario();
    }

    public void toBackInitial(View view) {
        Intent intent = new Intent(this, InitialActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        Toast.makeText(this, "Logando", Toast.LENGTH_SHORT).show();

        if(authLogin()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Senha incorreta. Tente novamente!", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean authLogin() {

        String password = editTextPassword.getText().toString();
        String email = editTextEmail.getText().toString();

        Usuario usuario = usuarioDao.authLogin(email, password);
        if(usuario != null){
            gravarSessao(usuario.getId());
            return true ;
        }
        return false ;
    }

    public void iniciarBancoUsuario(){
        usuarioDao = Room.databaseBuilder(getApplicationContext(), UsuarioRoom.class, "usuario.db")
                .allowMainThreadQueries()
                .build()
                .getRoomUsuarioDao();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void gravarSessao(int idUser) {
        SharedPreferences sharedPreferences = getSharedPreferences("dadosUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String idUserString = String.valueOf(idUser) ;
        editor.putString("isUser", idUserString);
        boolean resposta = editor.commit();
        if(resposta){
            Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT);
        }else{
            Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_SHORT);
        }
    }

//    private void ler() {
//        SharedPreferences sharedPreferences = getSharedPreferences("dadosUser",MODE_PRIVATE);
//        if(sharedPreferences.contains("nome_usuario") && sharedPreferences.contains("cor_usuario")){
//            Toast.makeText(this,
//                    sharedPreferences.getString("nome_usuario","nenhum"),
//                    Toast.LENGTH_SHORT).show();
//            String cor = sharedPreferences.getString("cor_usuario","Branca");
//            setarCor(cor);
//        }else{
//            Toast.makeText(this, "Erro no arquivo :´( ", Toast.LENGTH_SHORT).show();
//        }
//    }

}