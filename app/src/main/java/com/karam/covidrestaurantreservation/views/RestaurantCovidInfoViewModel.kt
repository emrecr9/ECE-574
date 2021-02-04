package com.karam.covidrestaurantreservation.views

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.karam.covidrestaurantreservation.models.AdditionalRestaurantInfo
import com.karam.covidrestaurantreservation.models.CovidPrecautions
import com.karam.covidrestaurantreservation.models.ZomatoRestaurantDetails
import com.karam.covidrestaurantreservation.services.OpenTableAdditionInfoService
import com.karam.covidrestaurantreservation.services.OpenTableService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class RestaurantCovidInfoViewModel(
    private val opentableService: OpenTableService,
    private val openTableAdditionInfoService: OpenTableAdditionInfoService) : ViewModel(),
    LifecycleObserver {


    suspend fun getAdditionalRestaurantInfo(restaurant: ZomatoRestaurantDetails?): AdditionalRestaurantInfo =
        withContext(Dispatchers.IO) {
            restaurant?.run {
                opentableService.getRestaurantByName(this.name).restarauntList.takeIf { list -> list.isNotEmpty() }
                    ?.run {
                        val htmlResponse =
                           openTableAdditionInfoService
                                .getString(this[0].id)


                        val parsedHtml = Jsoup.parse(htmlResponse)

                        val cleaningAndSanitizingStrings = getCleaningAndSanitizingStrings(parsedHtml)
                        val physicalDistancingStrings = getPhysicalDistancingStrings(parsedHtml)
                        val personalProtectiveEquipmentStrings = getPersonalProtectiveEquipmentStrings(parsedHtml)
                        val screeningStrings = getScreeningStrings(parsedHtml)

                        val url = getImageUrl(parsedHtml)

                        AdditionalRestaurantInfo(
                            CovidPrecautions(
                                cleaningAndSanitizingStrings,
                                physicalDistancingStrings,
                                personalProtectiveEquipmentStrings,
                                screeningStrings
                            ), url
                        )
                    }
            }

        } ?: AdditionalRestaurantInfo(CovidPrecautions(listOf(), listOf(), listOf(), listOf()), "")


    private fun getImageUrl(parsedHtml: Document): String? {
        val photosElement =
            parsedHtml.body().child(0).child(1).child(0).child(0).child(0)
        val regex = "\\(.*\\)".toRegex()
        val styleAttribute = photosElement.select("div[style]").attr("style")
        val url = regex.find(styleAttribute)?.value?.drop(1)?.dropLast(1)
        return url
    }


    private fun getCleaningAndSanitizingStrings(parsedHtml: Document): List<String> = run {

        val safetyPrecautionsElement =
            parsedHtml.body().getElementById("safety-precautions")

        val cleaningAndSanitizingElement =
            safetyPrecautionsElement?.let {
                it.child(0).child(1).child(0).child(0).child(0)
            }

        cleaningAndSanitizingElement?.let {
            it.children().select("li").map { it.text() }
        } ?: listOf()
    }

    private fun getPhysicalDistancingStrings(parsedHtml: Document): List<String> = run {

        val safetyPrecautionsElement =
            parsedHtml.body().getElementById("safety-precautions")

        val physicalDistancingElement =
            safetyPrecautionsElement?.let {
                it.child(0).child(1).child(0).child(0).child(1)
            }

        physicalDistancingElement?.let {
            it.children().select("li").map { it.text() }
        } ?: listOf()
    }

    private fun getPersonalProtectiveEquipmentStrings(parsedHtml: Document): List<String> = run {

        val safetyPrecautionsElement =
            parsedHtml.body().getElementById("safety-precautions")

        val personalProtectiveEquipmentElement =
            safetyPrecautionsElement?.let {
                it.child(0).child(1).child(0).child(1).child(0)
            }

        personalProtectiveEquipmentElement
            ?.let { it.children().select("li").map { it.text() } } ?: listOf()
    }

    private fun getScreeningStrings(parsedHtml: Document): List<String> = run {

        val safetyPrecautionsElement =
            parsedHtml.body().getElementById("safety-precautions")

        val screeningElement =
            safetyPrecautionsElement?.let {
                it.child(0).child(1).child(0).child(1).child(1)
            }

        screeningElement
            ?.let { it.children().select("li").map { it.text() } } ?: listOf()
    }
}