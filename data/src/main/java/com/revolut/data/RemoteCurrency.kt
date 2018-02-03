package com.revolut.data

/**
 * POJO for response from server.
 *
 * Created on 01.02.2018.
 */
class RemoteCurrency {
    var base: String? = null
    var date: String = ""
    var rates: Map<String, Double> = HashMap()
}