package com.revolut.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created on 01.02.2018.
 */
@Dao
public interface CurrencyDao {
    @Insert(onConflict = REPLACE)
    void save(List<Currency> currencies);
    @Query("SELECT * FROM currency")
    List<Currency> load();
}
