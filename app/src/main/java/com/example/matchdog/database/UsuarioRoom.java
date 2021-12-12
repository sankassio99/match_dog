package com.example.matchdog.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.matchdog.database.dao.UsuarioDao;
import com.example.matchdog.models.Usuario;

@Database(entities = {Usuario.class}, version = 1)
public abstract class UsuarioRoom extends RoomDatabase {

    public abstract UsuarioDao getRoomUsuarioDao();
}
