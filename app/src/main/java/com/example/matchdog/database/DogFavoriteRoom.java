package com.example.matchdog.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.matchdog.database.dao.DogFavoriteDao;
import com.example.matchdog.database.dao.UsuarioDao;
import com.example.matchdog.models.DogFavorite;
import com.example.matchdog.models.Usuario;

@Database(entities = {DogFavorite.class}, version = 1)
public abstract class DogFavoriteRoom extends RoomDatabase {
    public abstract DogFavoriteDao getRoomDogFavoriteDao();
}
