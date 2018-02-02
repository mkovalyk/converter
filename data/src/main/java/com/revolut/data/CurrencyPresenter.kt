package com.revolut.data

import android.util.Log
import io.reactivex.Observable

/**
 * Created on 23.01.2018.
 */

class CurrencyPresenter(val dao: CurrencyDao, val service: CurrencyWebService,
                        val schedulersFactory: SchedulersFactory, var view: CurrencyView?) {

    fun getCurrencies(base: Currency, loadFromLocal: Boolean) {
        if (loadFromLocal) {
            loadFromLocal()
        }
        service.latestCurrencies(base.key)
                .map { remoteCurrency -> remoteCurrency.toCurrencyList() }
                .doOnNext { currencies ->
                    dao.save(currencies)
                    Log.d("Test", "Before loading ${Thread.currentThread().id}")
                }
                .subscribeOn(schedulersFactory.background())
                .observeOn(schedulersFactory.main())
                .subscribe(
                        { },
                        { error -> throw error },
                        { loadFromLocal() }
                )
    }

    private fun loadFromLocal() {
        view?.loadingStarted()
        Observable.defer {
            run {
                Log.d("Test", "Before loading ${Thread.currentThread().id}")
            }
            createDelayed(dao.load())
        }
                .subscribeOn(schedulersFactory.background())
                .observeOn(schedulersFactory.main())
                .subscribe(
                        { view?.add(it) },
                        { error -> throw error },
                        {
                            view?.loadingCompleted()
                        })
    }

    private fun createDelayed(list: List<Currency>): Observable<Currency> {
        return Observable.create { emitter ->
            try {
                for (value in list) {
                    emitter.onNext(value)
                    Thread.sleep(50)
                }
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(ex)
            }
        }
    }
}
