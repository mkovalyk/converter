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
    var name: String
    var multiplier: Double
    var position: Int = 0
    @Ignore
    var value: Double? = null

    constructor() : this("")

    @Ignore
    constructor(key: String, name: String = "", multiplier: Double = 1.0, position: Int = 0) {
        this.key = key
        this.name = name
        this.multiplier = multiplier
        this.position = position
    }


    override fun toString(): String {
        return "Currency(key='$key', name=$name, multiplier=$multiplier), value=$value, position=$position"
    }

    fun contentSame(another: Currency): Boolean {
        return key == another.key && name == another.name && multiplier == another.multiplier
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Currency

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}