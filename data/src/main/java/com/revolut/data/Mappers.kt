package com.revolut.data

/**
 * Created on 01.02.18.
 */
fun RemoteCurrency?.toCurrencyList(): List<Currency> {
    val list = ArrayList<Currency>()
    if (this == null) return list
    var position = 0;
    // add base currency
    if (this.base != null) {
        list.add(Currency(this.base!!, position = position++))
    }
    for (currency in this.rates) {
        list.add(Currency(currency.key, currency.key, currency.value, position++))
    }
    return list
}
