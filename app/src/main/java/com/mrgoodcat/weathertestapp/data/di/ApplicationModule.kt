package com.mrgoodcat.weathertestapp.data.di

import android.content.Context
import androidx.room.Room
import com.mrgoodcat.weathertestapp.R
import com.mrgoodcat.weathertestapp.data.db.WeatherDatabase
import com.mrgoodcat.weathertestapp.data.db.WeatherTypeConverter
import com.mrgoodcat.weathertestapp.data.network.NetworkService
import com.mrgoodcat.weathertestapp.data.network.NetworkServiceImpl
import com.mrgoodcat.weathertestapp.data.repository.DbRepositoryImpl
import com.mrgoodcat.weathertestapp.data.repository.ErrorRepositoryImpl
import com.mrgoodcat.weathertestapp.data.repository.LocationApiImpl
import com.mrgoodcat.weathertestapp.data.repository.WeatherApiImpl
import com.mrgoodcat.weathertestapp.domain.repository.DbRepository
import com.mrgoodcat.weathertestapp.domain.repository.ErrorRepository
import com.mrgoodcat.weathertestapp.domain.repository.LocationApi
import com.mrgoodcat.weathertestapp.domain.repository.WeatherApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context, moshi: Moshi): NetworkService {
        return NetworkServiceImpl(context, moshi).getServiceInstance()
    }

    @Provides
    @Singleton
    fun provideLocationApi(@ApplicationContext context: Context): LocationApi {
        return LocationApiImpl(context)
    }

    @Provides
    @Singleton
    fun provideWeatherApi(
        @ApplicationContext context: Context,
        networkService: NetworkService
    ): WeatherApi {
        return WeatherApiImpl(context, networkService)
    }

    @Provides
    @Singleton
    fun providesHitMeUpDatabase(@ApplicationContext app: Context, moshi: Moshi): WeatherDatabase {
        return Room
            .databaseBuilder(app, WeatherDatabase::class.java, app.getString(R.string.room_db_name))
            .addTypeConverter(WeatherTypeConverter(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun providesDbRepository(db: WeatherDatabase): DbRepository {
        return DbRepositoryImpl(
            db = db
        )
    }

    @Provides
    @Singleton
    fun provideErrorRepository(@ApplicationContext context: Context): ErrorRepository {
        return ErrorRepositoryImpl(context)
    }
}