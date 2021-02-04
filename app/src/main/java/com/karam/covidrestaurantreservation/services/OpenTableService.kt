package com.karam.covidrestaurantreservation.services

import com.karam.covidrestaurantreservation.models.OpenTableRestaurantResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenTableService : Service {

    @GET("restaurants")
    suspend fun getRestaurantByName(@Query("name") name:String): OpenTableRestaurantResponse

}