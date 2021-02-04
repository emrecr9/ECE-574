package com.karam.covidrestaurantreservation.views

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
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
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class ReservingTablesFunctionalRequirements {


    @After
    fun teardown() {
        Reservations.reservationList= mutableSetOf()
    }

    @Test
    fun shouldSeeReservationScreenIfRestaurantPermitsReservations() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "El Barzon",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "12345",
            highlights = listOf("Table booking recommended"),
            covidCasesRateInfo = CovidCasesRateInfo(2000, 52314, 300, 8000)
        )
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            RestaurantDetailsActivity::class.java
        ).also {
            it.putExtra("restaurant", zomatoRestaurantDetails)
            it.putExtra("covid_rate_info", CovidCasesRateInfo(2000, 52314, 300, 8000))
        }
        val scenario = ActivityScenario.launch<RestaurantDetailsActivity>(intent)
        Thread.sleep(5000)

        onView(withId(R.id.restaurant_details_tab_layout).child(0).child(2)).perform(click())
        onView(withId(R.id.number_of_people_recyclerview)).check(matches(isDisplayed()))
        onView(withId(R.id.available_date_recyclerview)).check(matches(isDisplayed()))
        onView(withId(R.id.available_times_recyclerview)).check(matches(isDisplayed()))
        onView(withText("This restaurant does not allow reservations")).check(matches(not(isDisplayed())))
    }


    @Test
    fun shouldSeeErrorMessageIfRestaurantDoesNotPermitReservations() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "El Barzon",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "12345",
            highlights = listOf(),
            covidCasesRateInfo = CovidCasesRateInfo(2000, 52314, 300, 8000)
        )
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            RestaurantDetailsActivity::class.java
        ).also {
            it.putExtra("restaurant", zomatoRestaurantDetails)
            it.putExtra("covid_rate_info", CovidCasesRateInfo(2000, 52314, 300, 8000))
        }
        val scenario = ActivityScenario.launch<RestaurantDetailsActivity>(intent)
        Thread.sleep(5000)

        onView(withId(R.id.restaurant_details_tab_layout).child(0).child(2)).perform(click())
        onView(withId(R.id.number_of_people_recyclerview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.available_date_recyclerview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.available_times_recyclerview)).check(matches(not(isDisplayed())))
        onView(withText("This restaurant does not allow reservations")).check(matches(isDisplayed()))
    }


    @Test
    fun shouldBeAbleToChooseTheNumberOfPeopleWhenReserving() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "El Barzon",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "12345",
            highlights = listOf("Table booking recommended"),
            covidCasesRateInfo = CovidCasesRateInfo(2000, 52314, 300, 8000)
        )
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            RestaurantDetailsActivity::class.java
        ).also {
            it.putExtra("restaurant", zomatoRestaurantDetails)
            it.putExtra("covid_rate_info", CovidCasesRateInfo(2000, 52314, 300, 8000))
        }
        val scenario = ActivityScenario.launch<RestaurantDetailsActivity>(intent)
        Thread.sleep(5000)

        assertTrue(Reservations.reservationList.isEmpty())

        onView(withId(R.id.restaurant_details_tab_layout).child(0).child(2)).perform(click())
        onView(withId(R.id.number_of_people_recyclerview).child(0).child(0)).perform(click())
        onView(allOf(withId(R.id.number_of_people_recyclerview).child(0).child(0),withDrawable(R.drawable.filledcircle))).check(
            matches(isDisplayed()))

    }

    @Test
    fun shouldBeAbleToChooseTheDateWhenReserving() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "El Barzon",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "12345",
            highlights = listOf("Table booking recommended"),
            covidCasesRateInfo = CovidCasesRateInfo(2000, 52314, 300, 8000)
        )
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            RestaurantDetailsActivity::class.java
        ).also {
            it.putExtra("restaurant", zomatoRestaurantDetails)
            it.putExtra("covid_rate_info", CovidCasesRateInfo(2000, 52314, 300, 8000))
        }
        val scenario = ActivityScenario.launch<RestaurantDetailsActivity>(intent)
        Thread.sleep(5000)

        assertTrue(Reservations.reservationList.isEmpty())

        onView(withId(R.id.restaurant_details_tab_layout).child(0).child(2)).perform(click())
        onView(withId(R.id.available_date_recyclerview).child(3).child(0)).perform(click())
        onView(allOf(withId(R.id.available_date_recyclerview).child(3).child(0),withDrawable(R.drawable.filledcircle))).check(
            matches(isDisplayed()))

    }

    @Test
    fun shouldBeAbleToChooseTheTimeWhenReserving() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "El Barzon",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "12345",
            highlights = listOf("Table booking recommended"),
            covidCasesRateInfo = CovidCasesRateInfo(2000, 52314, 300, 8000)
        )
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            RestaurantDetailsActivity::class.java
        ).also {
            it.putExtra("restaurant", zomatoRestaurantDetails)
            it.putExtra("covid_rate_info", CovidCasesRateInfo(2000, 52314, 300, 8000))
        }
        val scenario = ActivityScenario.launch<RestaurantDetailsActivity>(intent)
        Thread.sleep(5000)

        assertTrue(Reservations.reservationList.isEmpty())

        onView(withId(R.id.restaurant_details_tab_layout).child(0).child(2)).perform(click())
        onView(withId(R.id.available_times_recyclerview).child(2).child(0)).perform(click())
        onView(allOf(withId(R.id.available_times_recyclerview).child(2).child(0),withDrawable(R.drawable.filledcircle))).check(
            matches(isDisplayed()))

    }

    @Test
    fun shouldUpdateReservationListAndGoToHomeScreenAfterReserving() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "El Barzon",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "12345",
            highlights = listOf("Table booking recommended"),
            covidCasesRateInfo = CovidCasesRateInfo(2000, 52314, 300, 8000)
        )
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            RestaurantDetailsActivity::class.java
        ).also {
            it.putExtra("restaurant", zomatoRestaurantDetails)
            it.putExtra("covid_rate_info", CovidCasesRateInfo(2000, 52314, 300, 8000))
        }
        val scenario = ActivityScenario.launch<RestaurantDetailsActivity>(intent)
        Thread.sleep(5000)

        assertTrue(Reservations.reservationList.isEmpty())

        onView(withId(R.id.restaurant_details_tab_layout).child(0).child(2)).perform(click())
        onView(withId(R.id.number_of_people_recyclerview).child(0).child(0)).perform(click())
        onView(withId(R.id.available_date_recyclerview).child(1).child(0)).perform(click())
        onView(withId(R.id.available_times_recyclerview).child(2).child(0)).perform(click())
        onView(withId(R.id.reserve_now_button)).perform(click())
        assertTrue(Reservations.reservationList.isNotEmpty())
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))

    }

    @Test
    fun reservingShouldNotTakeMoreThan10Seconds() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "El Barzon",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "12345",
            highlights = listOf("Table booking recommended"),
            covidCasesRateInfo = CovidCasesRateInfo(2000, 52314, 300, 8000)
        )
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            RestaurantDetailsActivity::class.java
        ).also {
            it.putExtra("restaurant", zomatoRestaurantDetails)
            it.putExtra("covid_rate_info", CovidCasesRateInfo(2000, 52314, 300, 8000))
        }
        val scenario = ActivityScenario.launch<RestaurantDetailsActivity>(intent)
        Thread.sleep(5000)

        assertTrue(Reservations.reservationList.isEmpty())

        onView(withId(R.id.restaurant_details_tab_layout).child(0).child(2)).perform(click())
        onView(withId(R.id.number_of_people_recyclerview).child(0).child(0)).perform(click())
        onView(withId(R.id.available_date_recyclerview).child(1).child(0)).perform(click())
        onView(withId(R.id.available_times_recyclerview).child(2).child(0)).perform(click())
        val startTime = System.currentTimeMillis()
        onView(withId(R.id.reserve_now_button)).perform(click())
        val endTime = System.currentTimeMillis()
        val elapsedTime = (endTime - startTime) / 1000.0
        assertTrue(elapsedTime < 10)
        assertTrue(Reservations.reservationList.isNotEmpty())
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))

    }

    @Test
    fun reservingShouldNotTakeMoreThan2Seconds() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "El Barzon",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "12345",
            highlights = listOf("Table booking recommended"),
            covidCasesRateInfo = CovidCasesRateInfo(2000, 52314, 300, 8000)
        )
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            RestaurantDetailsActivity::class.java
        ).also {
            it.putExtra("restaurant", zomatoRestaurantDetails)
            it.putExtra("covid_rate_info", CovidCasesRateInfo(2000, 52314, 300, 8000))
        }
        val scenario = ActivityScenario.launch<RestaurantDetailsActivity>(intent)
        Thread.sleep(5000)

        assertTrue(Reservations.reservationList.isEmpty())

        onView(withId(R.id.restaurant_details_tab_layout).child(0).child(2)).perform(click())
        onView(withId(R.id.number_of_people_recyclerview).child(0).child(0)).perform(click())
        onView(withId(R.id.available_date_recyclerview).child(1).child(0)).perform(click())
        onView(withId(R.id.available_times_recyclerview).child(2).child(0)).perform(click())
        val startTime = System.currentTimeMillis()
        onView(withId(R.id.reserve_now_button)).perform(click())
        val endTime = System.currentTimeMillis()
        val elapsedTime = (endTime - startTime) / 1000.0
        assertTrue(elapsedTime < 2)
        assertTrue(Reservations.reservationList.isNotEmpty())
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))

    }

    private fun withDrawable(@DrawableRes id:Int):Matcher<View> =
        object:TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
            }

            override fun matchesSafely(view: View?): Boolean {
                val context = view?.context
                val expectedBitmap = context?.getDrawable(id)?.toBitmap()
                return view is ImageView && view.background.toBitmap().sameAs(expectedBitmap)
            }

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