package com.revolut.data

import java.util.concurrent.Executor

/**
 * Created on 23.01.2018.
 */

class CurrencyRepository(val executor: Executor, val dao: CurrencyDao) {
    fun getCurrencies(base: Currency): List<Currency> {
        return ArrayList()
    }
}
