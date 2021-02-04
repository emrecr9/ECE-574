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
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RestaurantInformationFunctionalRequirements {


    @Test
    fun shouldSeeRestaurantName() {
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
        onView(withId(R.id.restaurant_details_name)).check(
            matches(
                allOf(
                    withText("El Barzon"),
                    isDisplayed()
                )
            )
        )

    }

    @Test
    fun shouldSeeRestaurantAggregateRating() {
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
        onView(withId(R.id.restaurant_details_aggregate_rating)).check(
            matches(
                allOf(
                    withText("(221)"),
                    isDisplayed()
                )
            )
        )

    }

    @Test
    fun shouldSeeRestaurantPriceRange() {
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

        onView(withId(R.id.restaurant_details_price_range)).check(
            matches(
                allOf(
                    withText("$$$$$"),
                    isDisplayed()
                )
            )
        )
    }

    @Test
    fun shouldSeeReviewsDetailsIfReviewsAreAvailable() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "El Barzon",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "16987033",
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

        onView(withId(R.id.restaurant_details_tab_layout).child(0).child(1)).perform(click())
        onView(withId(R.id.reviews_recyclerview)).check(matches(hasMinimumChildCount(1)))
        onView(withText(("Could not load review data"))).check(matches(not(isDisplayed())))

    }


    @Test
    fun shouldSeeErrorMessageIfCannotLoadReviewDetails() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "El Barzon",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "1",
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

        onView(withId(R.id.restaurant_details_tab_layout).child(0).child(1)).perform(click())
        onView(withText(("Could not load review data"))).check(matches(isDisplayed()))
    }

    @Test
    fun shouldSeeRestaurantImageIfImageIsAvailable() {
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

        onView(
            allOf(
                withId(R.id.restaurant_details_image),
                not(withDrawable(android.R.drawable.ic_menu_gallery))
            )
        ).check(matches(allOf(isDisplayed())))
    }

    @Test
    fun shouldSeePlaceholderIfRestaurantImageIsNotAvailable() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "Dutch Goose",
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

        onView(
            allOf(
                withId(R.id.restaurant_details_image),
                withDrawable(android.R.drawable.ic_menu_gallery)
            )
        ).check(matches(allOf(isDisplayed())))
    }

    @Test
    fun shouldSeeRestaurantPhoneNumber() {
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

        onView(withId(R.id.restaurant_details_phone_number)).check(
            matches(
                allOf(
                    withText("313-221-2131"),
                    isDisplayed()
                )
            )
        )
    }

    @Test
    fun shouldSeeRestaurantCuisineType() {
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

        onView(withId(R.id.restaurant_details_cuisine_type)).check(
            matches(
                allOf(
                    withText("Mexican, American, Burger"),
                    isDisplayed()
                )
            )
        )
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
        onView(withText("This restaurant does not allow reservations")).check(
            matches(
                not(
                    isDisplayed()
                )
            )
        )
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
    fun shouldSeeRestaurantAddress() {
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

        onView(withId(R.id.restaurant_details_address)).check(
            matches(
                allOf(
                    withText("1670 Junction Avenue , Detroit"),
                    isDisplayed()
                )
            )
        )
    }

    @Test
    fun shouldSeeGoogleMapsButton() {
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

        onView(withId(R.id.google_maps_button)).check(matches(allOf(isDisplayed())))

    }

    @Test
    fun shouldNavigateToGoogleMapsAfterClickingDirectionsButton() {
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

        Intents.init()

        onView(withId(R.id.google_maps_button)).perform(click())

        intended(hasAction(Intent.ACTION_VIEW))
        intended(IntentMatchers.hasData(Uri.parse("geo:42.323971,-83.202873?q=${Uri.parse("1670 Junction Avenue , Detroit")}")))

        Intents.release()
    }


    @Test
    fun shouldSeeCovidRateInfo() {
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

        onView(withId(R.id.covid_info_recyclerview)).perform(
            (RecyclerViewActions.actionOnItemAtPosition<CovidInfoAccordionViewHolder>(
                0,
                click()
            ))
        )
        onView(withText(Matchers.startsWith("Total number of COVID cases"))).check(
            matches(
                withText(
                    "Total number of COVID cases: 52314"
                )
            )
        )
        onView(withText(Matchers.startsWith("Total number of COVID deaths"))).check(
            matches(
                withText(
                    "Total number of COVID deaths: 8000"
                )
            )
        )
        onView(withText(Matchers.startsWith("Covid case increase"))).check(matches(withText("Covid case increase in the past 7 days: 2000")))
        onView(withText(Matchers.startsWith("Covid death increase"))).check(matches(withText("Covid death increase in the past 7 days: 300")))

    }


    @Test
    fun shouldSeeCovidCleaningAndSanitizingPrecautionsIfAvailable() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "El Barzon",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "1",
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

        onView(withId(R.id.covid_info_recyclerview)).perform(
            (RecyclerViewActions.actionOnItemAtPosition<CovidInfoAccordionViewHolder>(
                1,
                click()
            ))
        )
        onView(withText(Matchers.startsWith("Surfaces sanitized"))).check(matches(withText("Surfaces sanitized between seatings")))
        onView(withText(Matchers.startsWith("Common areas"))).check(matches(withText("Common areas deep cleaned daily")))
        onView(withText(Matchers.startsWith("Sanitizer or"))).check(matches(withText("Sanitizer or wipes provided for customers")))
    }

    @Test
    fun shouldSeeErrorMessageIfRestaurantDoesNotHaveCleaningAndSanitizingPrecautions() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "Union Street",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "123",
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

        onView(withId(R.id.covid_info_recyclerview)).perform(
            (RecyclerViewActions.actionOnItemAtPosition<CovidInfoAccordionViewHolder>(
                1,
                click()
            ))
        )

        onView(withId(R.id.covid_info_recyclerview).child(1).child(3).child(0).child(0)).check(
            matches(withText("Could not load data"))
        )
    }

    @Test
    fun shouldSeeCovidPhysicalDistancingPrecautionsIfAvailable() {
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

        onView(withId(R.id.covid_info_recyclerview)).perform(
            (RecyclerViewActions.actionOnItemAtPosition<CovidInfoAccordionViewHolder>(
                2,
                click()
            ))
        )
        onView(withText(Matchers.startsWith("Limited number"))).check(matches(withText("Limited number of seated diners")))
        onView(withText(Matchers.startsWith("Distancing maintained"))).check(matches(withText("Distancing maintained in common areas")))
        onView(withText(Matchers.startsWith("Extra space"))).check(matches(withText("Extra space between tables")))
    }

    @Test
    fun shouldSeeErrorMessageIfRestaurantDoesNotHavePhysicalDistancingPrecautions() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "Union Street",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "123",
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

        onView(withId(R.id.covid_info_recyclerview)).perform(
            (RecyclerViewActions.actionOnItemAtPosition<CovidInfoAccordionViewHolder>(
                2,
                click()
            ))
        )

        onView(withId(R.id.covid_info_recyclerview).child(2).child(3).child(0).child(0)).check(
            matches(withText("Could not load data"))
        )
    }

    @Test
    fun shouldSeeCovidPersonalProtectiveEquipmentPrecautionsIfAvailable() {
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

        onView(withText("Covid Precautions: Personal Protective Equipment")).perform(
            ViewActions.scrollTo(),
            click()
        )
        onView(withText(Matchers.startsWith("Waitstaff"))).check(matches(withText("Waitstaff wear masks")))
        onView(withText(Matchers.startsWith("Diners"))).check(matches(withText("Diners must wear masks unless eating or drinking")))
    }


    @Test
    fun shouldSeeErrorMessageIfRestaurantDoesNotHavePersonalProtectiveEquipmentPrecautions() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "Union Street",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "123",
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

        onView(withId(R.id.covid_info_recyclerview)).perform(
            (RecyclerViewActions.actionOnItemAtPosition<CovidInfoAccordionViewHolder>(
                3,
                click()
            ))
        )

        onView(withId(R.id.covid_info_recyclerview).child(3).child(3).child(0).child(0)).check(
            matches(withText("Could not load data"))
        )
    }

    @Test
    fun shouldSeeCovidScreeningPrecautionsIfAvailable() {
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

        onView(withText("Covid Precautions: Screening")).perform(ViewActions.scrollTo(), click())
        onView(withText(Matchers.startsWith("Sick staff"))).check(matches(withText("Sick staff prohibited in the workplace")))


    }

    @Test
    fun shouldSeeErrorMessageIfRestaurantDoesNotHaveScreeningPrecautions() {
        val zomatoRestaurantDetails = ZomatoRestaurantDetails(
            name = "Union Street",
            priceRange = "2",
            phoneNumbers = "313-221-2131",
            cuisines = "Mexican, American, Burger",
            rating = UserRating("4.2"),
            review_count = "221",
            location = Location("1670 Junction Avenue , Detroit", "48122"),
            restaurantId = "123",
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

        onView(withId(R.id.covid_info_recyclerview)).perform(
            (RecyclerViewActions.actionOnItemAtPosition<CovidInfoAccordionViewHolder>(
                4,
                click()
            ))
        )

        onView(withId(R.id.covid_info_recyclerview).child(4).child(3).child(0).child(0)).check(
            matches(withText("Could not load data"))
        )
    }


    private fun withDrawable(@DrawableRes id: Int): Matcher<View> =
        object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
            }

            override fun matchesSafely(view: View?): Boolean {
                val context = view?.context
                val expectedBitmap = context?.getDrawable(id)?.toBitmap()
                return view is ImageView && view.drawable.toBitmap().sameAs(expectedBitmap)
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