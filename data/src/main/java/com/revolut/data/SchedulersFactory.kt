package com.revolut.data

import io.reactivex.Scheduler


/**
 * Factory which should provides Schedulers for background work and posting on main thread.
 *
 * Created on 02.02.2018.
 */
interface SchedulersFactory {
    fun background(): Scheduler

    fun main(): Scheduler
}