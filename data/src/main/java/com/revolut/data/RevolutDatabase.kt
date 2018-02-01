package com.revolut.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase


/**
 * Created on 01.02.2018.
 */
@Database(entities = [(Currency::class)], version = 1)
abstract class RevolutDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}