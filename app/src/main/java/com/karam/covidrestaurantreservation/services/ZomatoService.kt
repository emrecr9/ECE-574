package com.karam.covidrestaurantreservation.services

import com.karam.covidrestaurantreservation.models.ReviewResponse
import com.karam.covidrestaurantreservation.models.ZomatoLocationResponse
import com.karam.covidrestaurantreservation.models.ZomatoRestaurantResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ZomatoService : Service {


    @GET("search")
    suspend fun getRestaurants(
        @Query("entity_id") entityId:String,
        @Query("entity_type") entitytype: String
    ): ZomatoRestaurantResponse


    @GET("reviews")
    suspend fun getReviews(@Query("res_id") restaurantId: String): ReviewResponse


    @GET("locations")
    suspend fun getLocation(@Query("lat") latitude:String,@Query("lon") longitude:String) : ZomatoLocationResponse

}