package com.revolut.data

import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.async

/**
 * Created on 23.01.2018.
 */

class CurrencyRepository(val dao: CurrencyDao, val service: CurrencyWebService) {
    suspend fun getCurrencies(base: Currency, data: MutableLiveData<List<Currency>>, loadFromLocal: Boolean) {
        if (loadFromLocal) {
            val fromDb = async { dao.load() }
            data.value = fromDb.await()
        }
        val responce = async {
            val value = service.latestCurrencies(base.key).execute()
            val currencies = value.body().toCurrencyList()
            dao.save(currencies)
            dao.load()
        }
        data.value = responce.await()
    }
}
