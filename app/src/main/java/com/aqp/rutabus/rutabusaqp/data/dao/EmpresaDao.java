package com.aqp.rutabus.rutabusaqp.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.aqp.rutabus.rutabusaqp.data.entity.Empresa;
import java.util.List;

@Dao
public interface EmpresaDao {

    @Query("select * from empresa")
    List<Empresa> getAll();

    @Query("select * from empresa where alias=:alias")
    Empresa getPorAlias(String alias);

    @Query("select * from empresa where id=:id")
    Empresa getPorId(String id);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Empresa empresa);

    @Update
    void actualizar(Empresa empresa);

    @Delete
    void eliminar(Empresa empresa);

    @Delete
    void eliminarVarios(Empresa... empresas);
}
