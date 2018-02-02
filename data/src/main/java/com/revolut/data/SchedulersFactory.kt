package com.revolut.data

import io.reactivex.Scheduler


/**
 * Created on 02.02.2018.
 */
interface SchedulersFactory {
    fun background(): Scheduler
    fun main(): Scheduler
}