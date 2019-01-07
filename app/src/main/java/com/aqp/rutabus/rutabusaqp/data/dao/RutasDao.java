package com.aqp.rutabus.rutabusaqp.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.aqp.rutabus.rutabusaqp.data.entity.Rutas;

import java.util.List;

@Dao
public interface RutasDao {
    @Query("select * from rutas where ETID=:idEmpresa")
    List<Rutas> getByEmpresa(String idEmpresa);

    @Query("select * from rutas  where ETID=:idEmpresa order by id asc limit 1 ")
    Rutas getFirstR(String idEmpresa);

    @Query("select * from rutas  where ETID=:idEmpresa order by id desc limit 1 ")
    Rutas getLastR(String idEmpresa);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Rutas rutas);
}
