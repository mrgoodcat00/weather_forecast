package com.mrgoodcat.weathertestapp.data.network

import android.content.Context
import com.mrgoodcat.weathertestapp.R
import com.squareup.moshi.Moshi
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkServiceImpl(context: Context, moshi : Moshi) {

    private var retrofit: Retrofit

    init {
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(UrlParamsInterceptor(context))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.weather_api_url))
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    fun getServiceInstance(): NetworkService {
        return retrofit.create(NetworkService::class.java)
    }
}