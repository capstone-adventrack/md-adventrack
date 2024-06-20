package com.example.adventrack.di

import android.content.Context
import com.example.adventrack.BuildConfig
import com.example.adventrack.data.remote.network.LocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

//    @Provides
//    @Singleton
//    fun provideDataStore(context: Context): DataStoreManager {
//        return DataStoreManager(context)
//    }

    @Provides
    @Singleton
    fun provideNetworkInterceptorLocation(): OkHttpClient {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val authInterceptor = Interceptor { chain ->
            val req = chain.request()

            val requestHeaders = req.newBuilder()
                .addHeader(
                    "X-Api-Key",
                    BuildConfig.API_KEY_LOCATION
                )
                .build()
            chain.proceed(requestHeaders)
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)

        return httpClient.build()
    }

    @Provides
    @Singleton
    fun provideNetworkClient(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_LOCATION)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideLocationService(retrofit: Retrofit): LocationService =
        retrofit.create(LocationService::class.java)

}