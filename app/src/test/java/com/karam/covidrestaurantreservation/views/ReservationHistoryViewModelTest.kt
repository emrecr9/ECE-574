package com.karam.covidrestaurantreservation.views

import com.karam.covidrestaurantreservation.models.Reservation
import com.karam.covidrestaurantreservation.utils.Reservations
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class ReservationHistoryViewModelTest{


    @Test
    fun `upcoming list and past reservation list should be empty if there are no reservations`() {
        mockkObject(Reservations)
        every {  Reservations.reservationList } returns mutableSetOf()
        val subject = ReservationHistoryViewModel()

        assertEquals(0,subject.getUpcomingReservationList().size)
        assertEquals(0,subject.getPastReservationList().size)
        unmockkObject(Reservations)
    }

    @Test
    fun `upcoming list should not be empty if there is a future reservation`() {
        mockkObject(Reservations)
        mockkClass(Calendar::class,relaxed = true)
        val calendar = Calendar.getInstance().also { it.add(Calendar.DAY_OF_YEAR,1)}
        val reservationSet = mutableSetOf(Reservation("sample name","2",calendar))
        every {  Reservations.reservationList } returns reservationSet
        val subject = ReservationHistoryViewModel()

        assertEquals(1,subject.getUpcomingReservationList().size)
        assertEquals(0,subject.getPastReservationList().size)
    }

    @Test
    fun `upcoming list should not be empty if there is a past reservation`() {
        mockkObject(Reservations)
        mockkClass(Calendar::class,relaxed = true)
        val calendar = Calendar.getInstance().also { it.add(Calendar.DAY_OF_YEAR,-1)}
        val reservationSet = mutableSetOf(Reservation("sample name","2",calendar))
        every {  Reservations.reservationList } returns reservationSet
        val subject = ReservationHistoryViewModel()

        assertEquals(0,subject.getUpcomingReservationList().size)
        assertEquals(1,subject.getPastReservationList().size)
    }

    @Test
    fun `upcoming list should not be empty if there are past and future reservations`() {
        mockkObject(Reservations)
        mockkClass(Calendar::class,relaxed = true)
        val pastCalendar = Calendar.getInstance().also { it.add(Calendar.DAY_OF_YEAR,5)}
        val futureCalendar = Calendar.getInstance().also { it.add(Calendar.DAY_OF_YEAR,-1)}
        val reservationSet =
            mutableSetOf(Reservation("sample name","2",pastCalendar),Reservation("sample name","2",futureCalendar))
        every {  Reservations.reservationList } returns reservationSet
        val subject = ReservationHistoryViewModel()

        assertEquals(1,subject.getUpcomingReservationList().size)
        assertEquals(1,subject.getPastReservationList().size)
    }


}