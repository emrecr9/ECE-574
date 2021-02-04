package com.karam.covidrestaurantreservation.views

import android.content.Intent
import android.icu.text.DateFormat.DAY
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karam.covidrestaurantreservation.R
import com.karam.covidrestaurantreservation.adapters.CovidInfoAccordionViewHolder
import com.karam.covidrestaurantreservation.models.*
import com.karam.covidrestaurantreservation.utils.Reservations
import junit.framework.Assert.assertTrue
import org.hamcrest.*
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class ReservationInfoFunctionalRequirements {


    @After
    fun teardown() {
        Reservations.reservationList= mutableSetOf()
    }

    @Test
    fun shouldSeeNoUpcomingReservationMessageIfThereAreNoUpcomingReservations() {
        Reservations.reservationList = mutableSetOf()
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        Thread.sleep(1000)
        onView(allOf(withText("Reservations"), withResourceName("smallLabel"))).perform(click())
        onView(withText("No Upcoming Reservations")).check(matches(isDisplayed()))
        onView(withId(R.id.reservations_history_recyclerview)).check(matches(not(isDisplayed())))
    }

    @Test
    fun shouldSeeReservationNameIfUpcomingReservationExists() {
        val calendar = Calendar.getInstance()
        calendar.set(2099,8,22,12,0)
        Reservations.reservationList = mutableSetOf(Reservation("El Barzon","5", calendar))
        val scenario = ActivityScenario.launch(HomeActivity::class.java)

        Thread.sleep(1000)
        onView(allOf(withText("Reservations"), withResourceName("smallLabel"))).perform(click())
        onView(withId(R.id.reservations_history_recyclerview)).check(matches((isDisplayed())))
        onView(withId(R.id.reservation_name)).check(matches(withText("El Barzon")))
    }

    @Test
    fun shouldSeeNumberOfPeopleIfUpcomingReservationExists() {
        val calendar = Calendar.getInstance()
        calendar.set(2099,8,22,12,0)
        Reservations.reservationList = mutableSetOf(Reservation("El Barzon","5", calendar))
        val scenario = ActivityScenario.launch(HomeActivity::class.java)

        Thread.sleep(1000)
        onView(allOf(withText("Reservations"), withResourceName("smallLabel"))).perform(click())
        onView(withId(R.id.reservations_history_recyclerview)).check(matches((isDisplayed())))
        onView(withId(R.id.reservation_number_of_people)).check(matches(withText("5")))
    }

    @Test
    fun shouldSeeReservationDateIfUpcomingReservationExists() {
        val calendar = Calendar.getInstance()
        calendar.set(2099,8,22,12,0)
        Reservations.reservationList = mutableSetOf(Reservation("El Barzon","5", calendar))
        val scenario = ActivityScenario.launch(HomeActivity::class.java)

        Thread.sleep(1000)
        onView(allOf(withText("Reservations"), withResourceName("smallLabel"))).perform(click())
        onView(withId(R.id.reservations_history_recyclerview)).check(matches((isDisplayed())))
        onView(withId(R.id.reservation_date)).check(matches(withText("22 - Sep - 2099")))
    }

    @Test
    fun shouldSeeReservationTimeIfUpcomingReservationExists() {
        val calendar = Calendar.getInstance()
        calendar.set(2099,8,22,12,0)
        Reservations.reservationList = mutableSetOf(Reservation("El Barzon","5", calendar))
        val scenario = ActivityScenario.launch(HomeActivity::class.java)

        Thread.sleep(1000)
        onView(allOf(withText("Reservations"), withResourceName("smallLabel"))).perform(click())
        onView(withId(R.id.reservations_history_recyclerview)).check(matches((isDisplayed())))
        onView(withId(R.id.reservation_time)).check(matches(withText("12:00 PM")))
    }

    @Test
    fun shouldSeeNoPastReservationMessageIfThereAreNoPastReservations() {
        Reservations.reservationList = mutableSetOf()
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        Thread.sleep(1000)
        onView(allOf(withText("Reservations"), withResourceName("smallLabel"))).perform(click())
        onView(withText("Past Reservations")).perform(click())
        onView(withText("No Past Reservations")).check(matches(isDisplayed()))
        onView(withId(R.id.reservations_history_recyclerview)).check(matches(not(isDisplayed())))
    }

    @Test
    fun shouldSeeReservationNameIfPastReservationExists() {
        val calendar = Calendar.getInstance()
        calendar.set(2015,8,22,12,0)
        Reservations.reservationList = mutableSetOf(Reservation("El Barzon","5", calendar))
        val scenario = ActivityScenario.launch(HomeActivity::class.java)

        Thread.sleep(1000)
        onView(allOf(withText("Reservations"), withResourceName("smallLabel"))).perform(click())
        onView(withText("Past Reservations")).perform(click())
        onView(withId(R.id.reservations_history_recyclerview)).check(matches((isDisplayed())))
        onView(withId(R.id.reservation_name)).check(matches(withText("El Barzon")))
    }

    @Test
    fun shouldSeeNumberOfPeopleIPastReservationExists() {
        val calendar = Calendar.getInstance()
        calendar.set(2015,8,22,12,0)
        Reservations.reservationList = mutableSetOf(Reservation("El Barzon","7", calendar))
        val scenario = ActivityScenario.launch(HomeActivity::class.java)

        Thread.sleep(1000)
        onView(allOf(withText("Reservations"), withResourceName("smallLabel"))).perform(click())
        onView(withText("Past Reservations")).perform(click())
        onView(withId(R.id.reservations_history_recyclerview)).check(matches((isDisplayed())))
        onView(withId(R.id.reservation_number_of_people)).check(matches(withText("7")))
    }

    @Test
    fun shouldSeeReservationDateIfPastReservationExists() {
        val calendar = Calendar.getInstance()
        calendar.set(2015,8,22,12,0)
        Reservations.reservationList = mutableSetOf(Reservation("El Barzon","5", calendar))
        val scenario = ActivityScenario.launch(HomeActivity::class.java)

        Thread.sleep(1000)
        onView(allOf(withText("Reservations"), withResourceName("smallLabel"))).perform(click())
        onView(withText("Past Reservations")).perform(click())
        onView(withId(R.id.reservations_history_recyclerview)).check(matches((isDisplayed())))
        onView(withId(R.id.reservation_date)).check(matches(withText("22 - Sep - 2015")))
    }

    @Test
    fun shouldSeeReservationTimeIfPastReservationExists() {
        val calendar = Calendar.getInstance()
        calendar.set(2015,8,22,8,0)
        Reservations.reservationList = mutableSetOf(Reservation("El Barzon","5", calendar))
        val scenario = ActivityScenario.launch(HomeActivity::class.java)

        Thread.sleep(1000)
        onView(allOf(withText("Reservations"), withResourceName("smallLabel"))).perform(click())
        onView(withText("Past Reservations")).perform(click())
        onView(withId(R.id.reservations_history_recyclerview)).check(matches((isDisplayed())))
        onView(withId(R.id.reservation_time)).check(matches(withText("08:00 AM")))
    }

    @Test
    fun loadingUpcomingReservationsShouldNotTakeMoreThanTwoSeconds() {
        val calendar = Calendar.getInstance()
        calendar.set(2099,8,22,12,0)
        Reservations.reservationList = mutableSetOf(Reservation("El Barzon","5", calendar))
        val scenario = ActivityScenario.launch(HomeActivity::class.java)

        Thread.sleep(1000)
        val startTime = System.currentTimeMillis()
        onView(allOf(withText("Reservations"), withResourceName("smallLabel"))).perform(click())
        onView(withId(R.id.reservations_history_recyclerview)).check(matches((isDisplayed())))
        val endTime = System.currentTimeMillis()
        onView(withId(R.id.reservation_date)).check(matches(withText("22 - Sep - 2099")))
        val elapsedTime = (endTime - startTime) / 1000.0
        assertTrue(elapsedTime < 2)
    }

    @Test
    fun loadingPastReservationsShouldNotTakeMoreThanTwoSeconds() {
        val calendar = Calendar.getInstance()
        calendar.set(2015,8,22,12,0)
        Reservations.reservationList = mutableSetOf(Reservation("El Barzon","5", calendar))
        val scenario = ActivityScenario.launch(HomeActivity::class.java)

        Thread.sleep(1000)
        val startTime = System.currentTimeMillis()
        onView(allOf(withText("Reservations"), withResourceName("smallLabel"))).perform(click())
        onView(withText("Past Reservations")).perform(click())
        onView(withId(R.id.reservations_history_recyclerview)).check(matches((isDisplayed())))
        val endTime = System.currentTimeMillis()
        val elapsedTime = (endTime - startTime) / 1000.0
        assertTrue(elapsedTime < 2)

    }


    private fun Matcher<View>.child(position: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
            }

            override fun matchesSafely(view: View?): Boolean {
                if (view?.parent !is ViewGroup)
                    return this@child.matches(view?.parent)
                else {
                    val viewGroup = view.parent as ViewGroup
                    return this@child.matches(viewGroup) && viewGroup.getChildAt(position) == view
                }
            }
        }
    }

}