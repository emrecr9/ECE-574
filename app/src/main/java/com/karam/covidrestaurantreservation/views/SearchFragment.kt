package com.karam.covidrestaurantreservation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.karam.covidrestaurantreservation.R
import com.karam.covidrestaurantreservation.adapters.SearchResultsAdapter
import com.karam.covidrestaurantreservation.models.ZomatoRestaurant
import com.karam.covidrestaurantreservation.models.ZomatoRestaurantDetails
import com.karam.covidrestaurantreservation.services.CovidService
import com.karam.covidrestaurantreservation.services.ZomatoService
import com.karam.covidrestaurantreservation.utils.RetrofitBuilder
import com.karam.covidrestaurantreservation.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), LifecycleObserver {

    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var oldRestaurantList: List<ZomatoRestaurantDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchResultsAdapter = SearchResultsAdapter(requireActivity())
        Places.initialize(requireContext(), "AIzaSyDidindStQlaFYQ2dSqzHL164vEyGTajMc")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel:SearchViewModel by viewModels {

            ViewModelFactory(
                RetrofitBuilder
                    .provideRetrofit("https://localcoviddata.com/covid19/v1/cases/")
                .create(CovidService::class.java),
                RetrofitBuilder
                    .provideRetrofit("https://developers.zomato.com/api/v2.1/")
                    .create(ZomatoService::class.java)
            )
        }
        view.restaurants_recyclerview.apply {
            adapter = searchResultsAdapter
            layoutManager = LinearLayoutManager(context)
        }

        val autoCompleteFragment = AutocompleteSupportFragment.newInstance()

        fragmentManager?.beginTransaction()?.replace(R.id.search_bar_container, autoCompleteFragment)?.commit()

        autoCompleteFragment.setCountries("US","CA","MX","NI","HN")
        autoCompleteFragment.setPlaceFields(listOf(Place.Field.NAME, Place.Field.LAT_LNG,Place.Field.ID))
        autoCompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {

            override fun onPlaceSelected(place: Place) {
                val restaurantListResponse = lifecycleScope.async {
                    autoCompleteFragment.setText(place.name)
                    place.latLng?.let {
                        viewModel.performSearch(it.latitude.toString(), it.longitude.toString())
                    } ?: listOf()
                }

                lifecycleScope.launch(Dispatchers.Main) {
                    view.search_loading_spinner.visibility = View.VISIBLE
                    view.restaurants_recyclerview.visibility = View.GONE
                    searchResultsAdapter.restaurantList = restaurantListResponse.await()
                    oldRestaurantList = searchResultsAdapter.restaurantList
                    search_loading_spinner.visibility = View.GONE
                    view.restaurants_recyclerview.visibility = View.VISIBLE
                }
            }

            override fun onError(status: Status) {
                println("an error occurred ${status.statusMessage}")
            }

        })
        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        view.allows_reservations_checkbox.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                searchResultsAdapter.restaurantList =
                    searchResultsAdapter.restaurantList.filter { it.highlights.contains("Table booking recommended") }
            } else {
                searchResultsAdapter.restaurantList = oldRestaurantList
            }
        }
    }

    override fun onResume() {
        super.onResume()
        searchResultsAdapter.notifyDataSetChanged()
    }

}