package com.example.matchdog.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.matchdog.models.DogFavorite;
import com.example.matchdog.models.Usuario;

import java.util.List;

@Dao
public interface DogFavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void salvar(DogFavorite jogo);

    @Query("SELECT * FROM dogfavorite")
    List<DogFavorite> todos();

    @Query("SELECT * FROM dogfavorite WHERE idUser=:idUser")
    List<DogFavorite> todosByUser(String idUser);

    @Query("SELECT * FROM dogfavorite WHERE id =:id")
    DogFavorite getById(String id);
}
