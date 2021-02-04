package com.karam.covidrestaurantreservation.views

import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karam.covidrestaurantreservation.R
import com.karam.covidrestaurantreservation.adapters.SearchResultsViewHolder
import junit.framework.Assert.assertTrue
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SearchFunctionalRequirementTests {


    @Test
    fun shouldNotSeeSearchSuggestionsIfInvalidTextIsEntered() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.places_autocomplete_search_input)).perform(typeText("ahasdfjhasdfjhgaskjdfgkjasdhfkj"))
        Thread.sleep(2900)
        onView(withId(R.id.places_autocomplete_list)).check(matches(hasChildCount(0)))
    }

    @Test
    fun shouldSeeSearchSuggestionsIfLocationIsEntered() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.places_autocomplete_search_input)).perform(typeText("1670 Heather"))
        Thread.sleep(2900)
        onView(withId(R.id.places_autocomplete_list)).check(matches(hasMinimumChildCount(1)))
        onView(withId(R.id.places_autocomplete_list)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldSeeCovidRateInfoInSearchResults() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.places_autocomplete_search_input)).perform(typeText("North Carolina"))
        Thread.sleep(2900)
        onView(withId(R.id.places_autocomplete_list)).perform(
            (RecyclerViewActions.actionOnItemAtPosition<SearchResultsViewHolder>(
                0,
                click()
            ))
        )
        Thread.sleep(2900)
        onView(withId(R.id.restaurants_recyclerview).child(0).child(0).child(4)).check(matches(isDisplayed()))
    }

 @Test
    fun shouldSeeLowCovidRateRestaurantsFirstInSearchResults() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.places_autocomplete_search_input)).perform(typeText("North Carolina"))
        Thread.sleep(2900)
        onView(withId(R.id.places_autocomplete_list)).perform(
            (RecyclerViewActions.actionOnItemAtPosition<SearchResultsViewHolder>(
                0,
                click()
            ))
        )
     Thread.sleep(2900)
        onView(withId(R.id.restaurants_recyclerview).child(0).child(0).child(4)).check(matches(allOf(isDisplayed(),
            withText("Low COVID rate"))))
    }

    @Test
    fun shouldSeeHighCovidRateRestaurantsLastInSearchResults() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.places_autocomplete_search_input)).perform(typeText("North Carolina"))
        Thread.sleep(2900)
        onView(withId(R.id.places_autocomplete_list)).perform(
            (RecyclerViewActions.actionOnItemAtPosition<SearchResultsViewHolder>(
                0,
                click()
            ))
        )
        Thread.sleep(2900)
        onView(withId(R.id.restaurants_recyclerview).child(3).child(0).child(4)).check(matches(allOf(isDisplayed(),
            withText("High COVID rate"))))
    }

    @Test
    fun searchingForARestaurantShouldNotTakeMoreThanThreeSeconds() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.places_autocomplete_search_input)).perform(typeText("1670 Junc"))
        Thread.sleep(2900)
        onView(withId(R.id.places_autocomplete_list)).perform(
            (RecyclerViewActions.actionOnItemAtPosition<SearchResultsViewHolder>(
                0,
                click()
            ))
        )
        Thread.sleep(2900)
        onView(withId(R.id.restaurants_recyclerview)).check(matches(allOf(isDisplayed(), hasMinimumChildCount(1))) )

    }

    @Test
    fun loadingRestaurantDetailsShouldNotTakeMoreThanTwoSeconds() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.places_autocomplete_search_input)).perform(typeText("1670 junction"))
        Thread.sleep(2900)
        onView(withId(R.id.places_autocomplete_list)).perform(
            (RecyclerViewActions.actionOnItemAtPosition<SearchResultsViewHolder>(
                0,
                click()
            ))
        )
        Thread.sleep(2900)
        onView(withId(R.id.restaurants_recyclerview).child(1).child(0)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.covid_info_recyclerview)).check(matches(isDisplayed()))
    }

    @Test
    fun loadingRestaurantDetailsForRestaurantWithExtraInfoShouldNotTakeMoreThanThreeSeconds() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.places_autocomplete_search_input)).perform(typeText("1670 junction"))
        Thread.sleep(2900)
        onView(withId(R.id.places_autocomplete_list)).perform(
            (RecyclerViewActions.actionOnItemAtPosition<SearchResultsViewHolder>(
                0,
                click()
            ))
        )
        Thread.sleep(2900)
        onView(withId(R.id.restaurants_recyclerview).child(0).child(0)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.covid_info_recyclerview)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldSeeSearchSuggestionsIfAddressIsInNorthAmerica() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.places_autocomplete_search_input)).perform(typeText("76 Clinton Street Lexington"))
        Thread.sleep(2900)
        onView(withId(R.id.places_autocomplete_list)).check(matches(hasMinimumChildCount(1)))
        onView(withId(R.id.places_autocomplete_list)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldNotSeeSearchSuggestionsIfAddressIsInAustralia() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.places_autocomplete_search_input)).perform(typeText("1 MacKay Eungella Rd"))
        Thread.sleep(2900)
        onView(withId(R.id.places_autocomplete_list)).check(matches(hasChildCount(0)))
    }

    @Test
    fun shouldNotSeeSearchSuggestionsIfAddressIsInSouthAmerica() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.places_autocomplete_search_input)).perform(typeText("Travessa Caligasta 905"))
        Thread.sleep(2900)
        onView(withId(R.id.places_autocomplete_list)).check(matches(hasChildCount(0)))
    }

    @Test
    fun shouldNotSeeSearchSuggestionsIfAddressIsInAsia() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.places_autocomplete_search_input)).perform(typeText("Blk 335 Kang Ching Road"))
        Thread.sleep(2900)
        onView(withId(R.id.places_autocomplete_list)).check(matches(hasChildCount(0)))
    }

    @Test
    fun shouldNotSeeSearchSuggestionsIfAddressIsInEurope() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.places_autocomplete_search_input)).perform(typeText("11-15 Swallow St Mayfair"))
        Thread.sleep(2900)
        onView(withId(R.id.places_autocomplete_list)).check(matches(hasChildCount(0)))
    }

    @Test
    fun shouldNotSeeSearchSuggestionsIfAddressIsInAfrica() {
        val scenario = ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.places_autocomplete_search_input)).perform(typeText("22 Oxford Rd Parktown"))
        Thread.sleep(2900)
        onView(withId(R.id.places_autocomplete_list)).check(matches(hasChildCount(0)))
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