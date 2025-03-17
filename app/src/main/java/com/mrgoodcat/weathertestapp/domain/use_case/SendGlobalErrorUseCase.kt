package com.mrgoodcat.weathertestapp.domain.use_case

import com.mrgoodcat.weathertestapp.domain.repository.ErrorRepository
import javax.inject.Inject

class SendGlobalStringErrorUseCase @Inject constructor(
    private val errorRepository: ErrorRepository
) {
    fun execute(error: String?) {
        errorRepository.showAnError(error)
    }
}