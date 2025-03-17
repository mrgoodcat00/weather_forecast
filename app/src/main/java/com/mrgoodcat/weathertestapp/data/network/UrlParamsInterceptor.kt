package com.mrgoodcat.weathertestapp.data.network

import android.content.Context
import com.mrgoodcat.weathertestapp.R
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class UrlParamsInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val appId = context.resources.getString(R.string.weather_app_id)
        val units = context.resources.getString(R.string.weather_units)

        val request: Request = chain.request()

        val addUrlParam = request.url
            .newBuilder()
            .addQueryParameter("appid", appId)
            .addQueryParameter("units", units)
            .build()

        val newRequest = request.newBuilder().url(addUrlParam).build();

        return chain.proceed(newRequest);
    }
}