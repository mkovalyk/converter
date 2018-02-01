package com.revolut.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created on 01.02.2018.
 */
@Entity(tableName = "currencies")
class Currency {
    @PrimaryKey
    var key: String
    var name: String?
    var multiplier: Double

    constructor() : this("")

    @Ignore
    constructor(key: String, name: String, multiplier: Double) {
        this.key = key
        this.name = name
        this.multiplier = multiplier
    }

    @Ignore
    constructor(key: String) {
        this.key = key
        this.name = ""
        this.multiplier = 1.0
    }

    override fun toString(): String {
        return "Currency(key='$key', name=$name, multiplier=$multiplier)"
    }

    fun contentSame(another: Currency): Boolean {
        return key == another.key && name == another.name && multiplier == another.multiplier
    }
}