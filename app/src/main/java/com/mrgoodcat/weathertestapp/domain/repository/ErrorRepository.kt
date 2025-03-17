package com.mrgoodcat.weathertestapp.domain.repository

import io.reactivex.rxjava3.core.Observable

interface ErrorRepository {
    fun showAnError(text: String?)
    fun showAnError(error: Int)
    fun subscribeOnError() : Observable<String>
}