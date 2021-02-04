package com.karam.covidrestaurantreservation.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitBuilder {

    fun provideRetrofit(baseUrl:String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                OkHttpClient.Builder().addInterceptor {
                    it.proceed(
                        it.request().newBuilder()
                            .addHeader("user-key", "feb633ae039c3b962af2b08937684111").build()
                    )
                }.addInterceptor(HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BASIC })
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}