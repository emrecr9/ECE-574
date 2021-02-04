package com.karam.covidrestaurantreservation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.karam.covidrestaurantreservation.R
import com.karam.covidrestaurantreservation.adapters.ReviewsAdapter
import com.karam.covidrestaurantreservation.services.ZomatoService
import com.karam.covidrestaurantreservation.utils.RetrofitBuilder
import com.karam.covidrestaurantreservation.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_review_details.view.*
import kotlinx.coroutines.launch


class ReviewDetailsFragment : Fragment() {

    private var restaurantId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restaurantId = arguments?.getString("restaurant_id","")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_review_details, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(restaurantId:String) =
            ReviewDetailsFragment().apply {
                arguments = Bundle().also { it.putString("restaurant_id",restaurantId) }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reviewsAdapter = ReviewsAdapter()
        view.reviews_recyclerview.apply {
            adapter = reviewsAdapter
            layoutManager = LinearLayoutManager(context)
        }

        val viewModel:ReviewDetailsViewModel by viewModels {
            ViewModelFactory( RetrofitBuilder.provideRetrofit("https://developers.zomato.com/api/v2.1/")
                .create(ZomatoService::class.java))
        }
        lifecycle.addObserver(viewModel)

        lifecycleScope.launch {
            view.reviews_fragment_spinner.visibility = View.VISIBLE
            restaurantId?.let {
                val reviewList = viewModel.getReviewList(it)
                if(reviewList.isEmpty()) {
                    view.reviews_recyclerview.visibility = View.GONE
                    view.cannot_load_review_data.visibility = View.VISIBLE
                }
                else {
                    view.reviews_recyclerview.visibility = View.VISIBLE
                    view.cannot_load_review_data.visibility = View.GONE
                    reviewsAdapter.reviewList = viewModel.getReviewList(it)

                }
            }

            view.reviews_fragment_spinner.visibility = View.GONE
        }

    }
}