package com.karam.covidrestaurantreservation.views

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.karam.covidrestaurantreservation.models.Review
import com.karam.covidrestaurantreservation.services.ZomatoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReviewDetailsViewModel(private val zomatoService: ZomatoService) : ViewModel(), LifecycleObserver {

    suspend fun getReviewList(restaurantId: String): List<Review> = withContext(Dispatchers.IO) {
            restaurantId.toIntOrNull()?.run {
                zomatoService
                    .getReviews(this.toString())
                    .userReviews
            } ?: listOf()
    }

}