package com.example.matchdog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matchdog.database.UsuarioRoom;
import com.example.matchdog.database.dao.UsuarioDao;
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

import com.example.matchdog.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private TextView textViewEmail, textViewName ;
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        iniciarBancoUsuario();

        View headerView = navigationView.getHeaderView(0);

        textViewEmail = headerView.findViewById(R.id.textViewEmail);
        textViewName = headerView.findViewById(R.id.textViewName);

        textViewEmail.setText("Email...");
        textViewName.setText("Name...");

        lerSessao();

    }

    public void iniciarBancoUsuario(){
        usuarioDao = Room.databaseBuilder(getApplicationContext(), UsuarioRoom.class, "time.db")
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

    public void openProfile(View view) {
        Toast.makeText(this, "Opening Profile", Toast.LENGTH_SHORT).show();
//        textViewName.setText("Pai ta online");
    }


    private void lerSessao() {
        SharedPreferences sharedPreferences = getSharedPreferences("dadosUser",MODE_PRIVATE);
        String idUser =  sharedPreferences.getString("isUser","1");

        Usuario usuario = usuarioDao.getById(idUser);

        textViewName.setText(usuario.getName());
        textViewEmail.setText(usuario.getEmail());
    }

}