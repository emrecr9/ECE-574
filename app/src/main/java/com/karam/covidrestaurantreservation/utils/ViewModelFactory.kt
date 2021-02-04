package com.karam.covidrestaurantreservation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.karam.covidrestaurantreservation.services.*
import com.karam.covidrestaurantreservation.views.RestaurantCovidInfoViewModel
import com.karam.covidrestaurantreservation.views.ReviewDetailsViewModel
import com.karam.covidrestaurantreservation.views.SearchViewModel

class ViewModelFactory(vararg val services: Service) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return when (modelClass) {
            ReviewDetailsViewModel::class.java -> ReviewDetailsViewModel(services[0] as ZomatoService) as T
            SearchViewModel::class.java -> SearchViewModel(
                services[0] as CovidService, services[1] as ZomatoService ) as T
            else -> RestaurantCovidInfoViewModel(
                services[0] as OpenTableService,
                services[1] as OpenTableAdditionInfoService) as T
        }
    }
}