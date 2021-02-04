package com.karam.covidrestaurantreservation.views

import com.karam.covidrestaurantreservation.models.ReservationItem
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class ReserveViewModelTest {


    @Test
    fun getAvailableDatesList_shouldHaveDatesOfEntireYear() {
        val subject = ReserveViewModel()

        val resultList = subject.getAvailableDatesList()

        assertEquals(365,resultList.size)
    }

    @Test
    fun getAvailableDatesList_shouldNotHaveTodaysDate() {
        val subject = ReserveViewModel()

        val resultList = subject.getAvailableDatesList()
        val calendar = Calendar.getInstance()
        val todaysDate = ReservationItem(SimpleDateFormat("dd , MMM").format(calendar.time))
        assertNotEquals(todaysDate.title,resultList[0].title)
    }

    @Test
    fun getNumberOfPeopleList_shouldHaveMaximumOfTwentyPeople() {
        val subject = ReserveViewModel()

        val resultList = subject.getNumberOfPeopleList()

        assertEquals(20,resultList.size)
    }

    @Test
    fun getNumberOfPeopleList_firstItemShouldNotBeZero() {
        val subject = ReserveViewModel()

        val resultList = subject.getNumberOfPeopleList()

        assertNotEquals(0,resultList.first())
    }

    @Test
    fun getAvailableTimesList_lastItemShouldBe1130PM() {
        val subject = ReserveViewModel()

        val resultList = subject.getAvailableTimesList()

        assertEquals("11:30 PM",resultList.last().title)
    }

    @Test
    fun getAvailableTimesList_firstItemShouldBe9AM() {
        val subject = ReserveViewModel()

        val resultList = subject.getAvailableTimesList()

        assertEquals("9:00 AM",resultList.first().title)
    }



}