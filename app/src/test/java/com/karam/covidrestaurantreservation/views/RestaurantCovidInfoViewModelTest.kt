package com.karam.covidrestaurantreservation.views

import com.karam.covidrestaurantreservation.models.*
import com.karam.covidrestaurantreservation.services.OpenTableAdditionInfoService
import com.karam.covidrestaurantreservation.services.OpenTableService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

class RestaurantCovidInfoViewModelTest {

    private val openTableService:OpenTableService = mockk()
    private val openTableAdditionInfoService:OpenTableAdditionInfoService = mockk()

    @Test
    fun getAdditionalRestaurantInfo_givenRestaurantDetailsIsNull_returnsEmptyListsAndUrl() = runBlocking {
        val subject = RestaurantCovidInfoViewModel(openTableService,openTableAdditionInfoService)

        val result = subject.getAdditionalRestaurantInfo(null)

        assertEquals(AdditionalRestaurantInfo(CovidPrecautions(listOf(), listOf(), listOf()),""),result)
    }

    @Test
    fun getAdditionalRestaurantInfo_givenOpenTableGivesEmptyResponse_returnsEmptyListsAndUrl() = runBlocking {
        val openTableResponse = OpenTableRestaurantResponse(listOf())
        val zomatoRestaurantDetails : ZomatoRestaurantDetails = mockk(relaxed = true)
        every { zomatoRestaurantDetails.name } returns "name"
        coEvery { openTableService.getRestaurantByName("name") } returns openTableResponse
        val subject = RestaurantCovidInfoViewModel(openTableService,openTableAdditionInfoService)

        val result = subject.getAdditionalRestaurantInfo(zomatoRestaurantDetails)

        assertEquals(AdditionalRestaurantInfo(CovidPrecautions(listOf(), listOf(), listOf()),""),result)
    }

    @Test
    fun getAdditionalRestaurantInfo_givenOpenTableHasResponse_returnsImageUrl() = runBlocking {

        val openTableResponse = OpenTableRestaurantResponse(listOf(OpentableRestaurant("123")))
        val zomatoRestaurantDetails : ZomatoRestaurantDetails = mockk(relaxed = true)
        every { zomatoRestaurantDetails.name } returns "name"
        coEvery { openTableService.getRestaurantByName("name") } returns openTableResponse
        coEvery { openTableAdditionInfoService.getString("123") } returns  getHtmlString("restaurant_additional_info_tria_no_lists.txt")
        val subject = RestaurantCovidInfoViewModel(openTableService,openTableAdditionInfoService)

        val result = subject.getAdditionalRestaurantInfo(zomatoRestaurantDetails)
        assertEquals(AdditionalRestaurantInfo(CovidPrecautions(listOf(), listOf(), listOf())
            ,"https://resizer.otstatic.com/v2/photos/wide-huge/3/32330974.jpg"),result)
    }

    @Test
    fun getAdditionalRestaurantInfo_givenOpenTableHasResponse_returnsCovidPrecautionLists() = runBlocking {

        val openTableResponse = OpenTableRestaurantResponse(listOf(OpentableRestaurant("123")))
        val zomatoRestaurantDetails : ZomatoRestaurantDetails = mockk(relaxed = true)
        every { zomatoRestaurantDetails.name } returns "name"
        coEvery { openTableService.getRestaurantByName("name") } returns openTableResponse
        coEvery { openTableAdditionInfoService.getString("123") } returns  getHtmlString("restaurant_additional_info_tria_with_lists.txt")
        val subject = RestaurantCovidInfoViewModel(openTableService,openTableAdditionInfoService)

        val result = subject.getAdditionalRestaurantInfo(zomatoRestaurantDetails)
        assertNotNull(result.covidPrecautions.physicalDistancingList)
        assertNotNull(result.covidPrecautions.personalProtectiveEquipmentList)
        assertNotNull(result.covidPrecautions.cleaningAndSanitizingList)
    }


    private fun getHtmlString(filePath:String):String  {
        val basePath = System.getProperty("user.dir")
        val file = Files.readAllBytes(Paths.get("${basePath}/responses/$filePath"))
        return String(file)
    }

}