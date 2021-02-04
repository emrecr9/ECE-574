package com.karam.covidrestaurantreservation.services

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenTableAdditionInfoService : Service {

    @GET("single.aspx")
    suspend fun getString(@Query("rid") rid:String):String

}