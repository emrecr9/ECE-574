package com.karam.covidrestaurantreservation.views

import com.karam.covidrestaurantreservation.models.Review
import com.karam.covidrestaurantreservation.models.ReviewDetails
import com.karam.covidrestaurantreservation.models.ReviewResponse
import com.karam.covidrestaurantreservation.models.Reviewer
import com.karam.covidrestaurantreservation.services.ZomatoService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ReviewDetailsViewModelTest {

    private val zomatoService : ZomatoService = mockk()


    @Test
    fun getReviewList_givenResponseHasEmptyReviews_returnsEmptyList() = runBlocking {
        coEvery { zomatoService.getReviews("1") } returns ReviewResponse(listOf())
        val subject = ReviewDetailsViewModel(zomatoService)

        val result = subject.getReviewList("1")

        assertEquals(result, emptyList<Review>())
    }

    @Test
    fun getReviewList_givenRestaurantIdIsNotNumberString_returnsEmptyList() = runBlocking {
        val reviewDetails = ReviewDetails("5", Reviewer("john"),"good")
        val response = ReviewResponse(listOf(Review(reviewDetails)))
        coEvery { zomatoService.getReviews("not a number") } returns response
        val subject = ReviewDetailsViewModel(zomatoService)

        val result = subject.getReviewList("not a number")

        assertEquals(result, emptyList<Review>())
    }

    @Test
    fun getReviewList_givenRestaurantIdIsANumberString_returnsResponse() = runBlocking {
        val reviewDetails = ReviewDetails("5", Reviewer("john"),"good")
        val response = ReviewResponse(listOf(Review(reviewDetails)))
        coEvery { zomatoService.getReviews("12345") } returns response
        val subject = ReviewDetailsViewModel(zomatoService)

        val result = subject.getReviewList("12345")

        assertEquals(result, response.userReviews)
    }

}
