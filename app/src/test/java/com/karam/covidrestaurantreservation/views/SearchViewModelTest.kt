package com.karam.covidrestaurantreservation.views

import com.karam.covidrestaurantreservation.models.*
import com.karam.covidrestaurantreservation.services.CovidService
import com.karam.covidrestaurantreservation.services.ZomatoService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class SearchViewModelTest {


    private val covidRetrofitService: CovidService = mockk()
    private val zomatoService: ZomatoService = mockk()


    @Test
    fun performSearch_givenLatitudeIsInteger_returnsEmptyList() = runBlocking {
        val subject = SearchViewModel(covidRetrofitService, zomatoService)
        coEvery { zomatoService.getLocation("1","32.12") } returns ZomatoLocationResponse(listOf())
        val result = subject.performSearch("1","32.12")

        assertEquals(emptyList<ZomatoRestaurant>(),result)
    }

    @Test
    fun performSearch_givenLongitudeIsInteger_returnsEmptyList() = runBlocking {
        coEvery { zomatoService.getLocation("12.23","3") } returns ZomatoLocationResponse(listOf())
        val subject = SearchViewModel(covidRetrofitService, zomatoService)
        val result = subject.performSearch("12.23","3")

        assertEquals(emptyList<ZomatoRestaurant>(),result)
    }

    @Test
    fun performSearch_givenCovidInfoIsPresent_returnsMappedRestaurant() = runBlocking {
        val covidResponse: CovidResponse = mockk(relaxed = true)
        every { covidResponse.counties[0].historicData[0].positiveCasesCount } returns "10"
        every { covidResponse.counties[0].historicData[0].deathCount } returns "20"
        every { covidResponse.counties[0].historicData.last().positiveCasesCount } returns "3"
        every { covidResponse.counties[0].historicData.last().deathCount } returns "10"
        val zomatoLocationResponse:ZomatoLocationResponse = mockk(relaxed = true)
        val zomatoRestaurant:ZomatoRestaurant = mockk(relaxed = true)
        every { zomatoRestaurant.restaurant.location.zipCode } returns "123"
        every { zomatoLocationResponse.locationList[0].entityId } returns "12345"
        every { zomatoLocationResponse.locationList[0].entityType } returns "entity type"
        coEvery { zomatoService.getLocation("12.23","12.22") } returns zomatoLocationResponse
        coEvery { zomatoService.getRestaurants("12345","entity type") } returns ZomatoRestaurantResponse(
            listOf(zomatoRestaurant))
        coEvery { covidRetrofitService.getCovidDateForZipCode("123") } returns covidResponse
        val subject = SearchViewModel(covidRetrofitService, zomatoService)


        val result = subject.performSearch("12.23","12.22")
        assertNotEquals(0,result.size)

    }



}