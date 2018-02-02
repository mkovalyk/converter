package com.revolut.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Used as Java class for better highlighting
 * <p>
 * Created on 01.02.2018.
 */
@Dao
public interface CurrencyDao {
    @Query("SELECT * FROM currencies ORDER BY position")
    List<Currency> load();

    @Insert(onConflict = REPLACE)
    void save(List<Currency> currencies);
}
