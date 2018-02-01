package com.revolut.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created on 01.02.2018.
 */
@Entity
class Currency{
    @PrimaryKey
    val key: String
    var name: String?
    var multiplier: Double? = null

    constructor() {
        this.key = ""
        this.name = ""
    }

    @Ignore
    constructor(key: String, name: String, multiplier: Double) {
        this.key = key
        this.name = name
        this.multiplier = multiplier
    }
}