package com.aqp.rutabus.rutabusaqp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.aqp.rutabus.rutabusaqp.data.dao.EmpresaDao;
import com.aqp.rutabus.rutabusaqp.data.dao.RutasDao;
import com.aqp.rutabus.rutabusaqp.data.entity.Calificaciones;
import com.aqp.rutabus.rutabusaqp.data.entity.Empresa;
import com.aqp.rutabus.rutabusaqp.data.entity.Puntos;
import com.aqp.rutabus.rutabusaqp.data.entity.Rutas;

@Database(
            entities = {
                    Empresa.class,
                    Rutas.class,
                    Calificaciones.class,
                    Puntos.class
            },
        version = 1,
        exportSchema = false
)
public abstract class AppDataBase extends RoomDatabase {
    public static String DB_NAME = "rutabus.sqlite";
    public abstract EmpresaDao empresaDao();
    public abstract RutasDao rutasDao();

    private static AppDataBase instance;

    public static synchronized AppDataBase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, AppDataBase.class, DB_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }

}
