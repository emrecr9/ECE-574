package com.karam.covidrestaurantreservation.views

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.karam.covidrestaurantreservation.models.CovidCasesRateInfo
import com.karam.covidrestaurantreservation.models.ZomatoRestaurant
import com.karam.covidrestaurantreservation.models.ZomatoRestaurantDetails
import com.karam.covidrestaurantreservation.services.CovidService
import com.karam.covidrestaurantreservation.services.ZomatoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.set

class SearchViewModel(
    private val covidRetrofitService: CovidService,
    private val zomatoService: ZomatoService) : ViewModel(),LifecycleObserver {

    private val zipCodeCovidRateMap: HashMap<String, CovidCasesRateInfo> = hashMapOf()

    private suspend fun getRestaurantList(entityId:String,entityType:String):List<ZomatoRestaurantDetails> = withContext(Dispatchers.IO) {

        val restaurantList =  zomatoService.getRestaurants(entityId,entityType).restarauntList

        restaurantList.map { it.restaurant.location.zipCode }.toSet().forEach { zipCode ->
            val covidResponse = covidRetrofitService.getCovidDateForZipCode(zipCode)
            val covidIncreaseIn7Days =
                (covidResponse.counties[0].historicData[0].positiveCasesCount).toInt()
                    .minus((covidResponse.counties[0].historicData.last().positiveCasesCount).toInt())

            val totalCovidCases = covidResponse.counties[0].historicData[0].positiveCasesCount.toInt()

            val covidDeathIncreasePast7Days = (covidResponse.counties[0].historicData[0].deathCount).toInt()
                .minus((covidResponse.counties[0].historicData.last().deathCount).toInt())

            val totalCovidDeaths =  covidResponse.counties[0].historicData[0].deathCount.toInt()


            zipCodeCovidRateMap[zipCode] = CovidCasesRateInfo(covidIncreaseIn7Days,totalCovidCases,covidDeathIncreasePast7Days,totalCovidDeaths)
        }

        restaurantList.map { restaurant ->
            restaurant.restaurant.covidCasesRateInfo = zipCodeCovidRateMap[restaurant.restaurant.location.zipCode] ?:CovidCasesRateInfo(0,0,0,0)
            restaurant.restaurant
        }.sortedBy { it.covidCasesRateInfo.covidCaseIncreasePast7Days}

    }

    suspend fun performSearch(latitude:String,longitude:String):List<ZomatoRestaurantDetails> = withContext(Dispatchers.IO) {
        val zomatoLocationResponse = zomatoService.getLocation(latitude,longitude)
        zomatoLocationResponse.takeIf { it.locationList.isNotEmpty()
                && latitude.toDoubleOrNull() != null
                && longitude.toDoubleOrNull() != null
        }?.let {
            getRestaurantList(it.locationList[0].entityId,it.locationList[0].entityType)
        } ?: listOf()
    }


}