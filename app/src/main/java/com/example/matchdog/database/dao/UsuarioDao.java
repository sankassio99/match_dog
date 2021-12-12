package com.example.matchdog.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.matchdog.models.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void salvar(Usuario jogo);

    @Query("SELECT * FROM usuario")
    List<Usuario> todos();

    @Query("SELECT * FROM usuario WHERE email =:email AND password=:password")
    Usuario authLogin(String email, String password);

    @Query("SELECT * FROM usuario WHERE id =:id")
    Usuario getById(String id);

}
