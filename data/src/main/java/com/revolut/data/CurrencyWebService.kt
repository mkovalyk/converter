package com.revolut.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created on 01.02.2018.
 */
interface CurrencyWebService
{
//    https://revolut.duckdns.org/latest?base=EUR
    @GET("latest")
    fun latestCurrencies(@Query("base") base:String): Call<RemoteCurrency>
}