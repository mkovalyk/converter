package com.revolut.data

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API service
 *
 * Created on 01.02.2018.
 */
interface CurrencyWebService {
    @GET("latest")
    fun latestCurrencies(@Query("base") base: String): Observable<RemoteCurrency>
}