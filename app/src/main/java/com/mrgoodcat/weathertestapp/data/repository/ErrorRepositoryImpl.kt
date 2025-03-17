package com.mrgoodcat.weathertestapp.data.repository

import android.content.Context
import com.mrgoodcat.weathertestapp.R
import com.mrgoodcat.weathertestapp.domain.repository.ErrorRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class ErrorRepositoryImpl(val context: Context) : ErrorRepository {

    private var errorNotifier = PublishSubject.create<String>()

    override fun showAnError(text: String) {
        errorNotifier.onNext(text)
    }

    override fun showAnError(error: Int) {
        errorNotifier.onNext(mapErrorCode(error))
    }

    override fun subscribeOnError(): Observable<String> {
        return errorNotifier
    }

    private fun mapErrorCode(int: Int): String {
        val result = when (int) {
            1000 -> ""
            else -> context.getString(R.string.error_unknown_text)
        }

        return result
    }
}