package com.mrgoodcat.weathertestapp.domain.use_case

import com.mrgoodcat.weathertestapp.domain.repository.ErrorRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SubscribeToErrorUseCase @Inject constructor(
    private val errorRepository: ErrorRepository
) {
    fun execute(): Observable<String> {
        return errorRepository.subscribeOnError()
    }
}