package com.karam.covidrestaurantreservation.services

import com.karam.covidrestaurantreservation.models.CovidResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CovidService: Service{

    @GET("newYorkTimes")
    suspend fun getCovidDateForZipCode(@Query("zipCode") zipCode:String,@Query("daysInPast") daysInPast:Int = 7): CovidResponse
}