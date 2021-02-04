package com.karam.covidrestaurantreservation.views

import androidx.lifecycle.ViewModel
import com.karam.covidrestaurantreservation.models.Reservation
import com.karam.covidrestaurantreservation.utils.Reservations
import java.util.*

class ReservationHistoryViewModel : ViewModel() {


    fun getUpcomingReservationList(): List<Reservation> =
        Reservations.reservationList.toList().filter { it.reservationTimestamp.after(Calendar.getInstance())}

    fun getPastReservationList(): List<Reservation> =
        Reservations.reservationList.toList().filter { it.reservationTimestamp.before(Calendar.getInstance()) }


}