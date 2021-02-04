package com.karam.covidrestaurantreservation.views

import androidx.lifecycle.ViewModel
import com.karam.covidrestaurantreservation.models.ReservationItem
import com.karam.covidrestaurantreservation.utils.Reservations
import java.text.SimpleDateFormat
import java.util.*

class ReserveViewModel : ViewModel() {

    fun getAvailableDatesList(): List<ReservationItem> = run {

        val calendar = Calendar.getInstance()
        return (0..365).map {
            val today = calendar.time
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            ReservationItem(SimpleDateFormat("dd , MMM").format(today), today)
        }.drop(1)
    }

    fun getNumberOfPeopleList(): List<ReservationItem> =
        (1..20).map { integer -> ReservationItem(integer.toString()) }


    fun getAvailableTimesList(): List<ReservationItem> =
        Reservations.listOfAvailableTimes.map { availableTime ->
            ReservationItem(availableTime)
        }

}






