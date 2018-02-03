package com.revolut.data

/**
 * View for interaction with {@link CurrencyPresenter}
 *
 * Created on 02.02.2018.
 */
interface CurrencyView {
    fun onError(error: Throwable)

    fun add(currency: Currency)

    fun loadingStarted()

    fun loadingCompleted()
}