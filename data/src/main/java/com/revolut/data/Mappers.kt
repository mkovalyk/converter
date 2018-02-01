package com.revolut.data

/**
 * Created on 01.02.18.
 */
fun RemoteCurrency?.toCurrencyList(): List<Currency> {
    val list = ArrayList<Currency>()
    if (this == null) return list
    // add base currency
    if (this.base != null) {
        list.add(Currency(this.base!!))
    }
    for (currency in this.rates) {
        list.add(Currency(currency.key, "", currency.value))
    }
    return list
}
