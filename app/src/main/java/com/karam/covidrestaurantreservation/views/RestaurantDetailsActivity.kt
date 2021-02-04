package com.karam.covidrestaurantreservation.views

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.karam.covidrestaurantreservation.R
import com.karam.covidrestaurantreservation.models.CovidCasesRateInfo
import com.karam.covidrestaurantreservation.models.RestaurantUiModel
import com.karam.covidrestaurantreservation.models.ZomatoRestaurant
import com.karam.covidrestaurantreservation.models.ZomatoRestaurantDetails
import kotlinx.android.synthetic.main.activity_restaurant_details.*

class RestaurantDetailsActivity : AppCompatActivity(), LifecycleObserver {

    private lateinit var restaurant: ZomatoRestaurantDetails
    private lateinit var covidCasesRateInfo: CovidCasesRateInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)


        restaurant = intent.getParcelableExtra("restaurant") as ZomatoRestaurantDetails
        covidCasesRateInfo = intent.getParcelableExtra("covid_rate_info") as CovidCasesRateInfo



//        val restaurantCovidInfoFragment = RestaurantCovidInfoFragment.newInstance(restaurant, covidCasesRateInfo)
        val restaurantCovidInfoFragment:RestaurantCovidInfoFragment by lazy {
            RestaurantCovidInfoFragment.newInstance(restaurant, covidCasesRateInfo)
        }
        val reviewDetailsFragment:ReviewDetailsFragment by lazy {
            ReviewDetailsFragment.newInstance(restaurant.restaurantId)
        }
        val reserveFragment : ReserveFragment by lazy {
            ReserveFragment.newInstance(restaurant)
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.restaurant_details_fragment_container, restaurantCovidInfoFragment)
            .add(R.id.restaurant_details_fragment_container, reviewDetailsFragment)
            .add(R.id.restaurant_details_fragment_container, reserveFragment)
            .hide(reviewDetailsFragment)
            .hide(reserveFragment)
            .commit()

       val restaurantUiModel =  RestaurantUiModel(
            restaurantId = restaurant.restaurantId,
            restaurantName = restaurant.name,
            ratingText = restaurant.rating.aggregateRating,
            priceRange = "".padEnd(restaurant.priceRange.toInt(), '$'),
            reviewCount = if (restaurant.review_count.toInt() == 0)
                "No reviews available"
            else restaurant.review_count,
            phoneNumbers = restaurant.phoneNumbers,
            cuisines = restaurant.cuisines,
            address = restaurant.location.address,
            aggregateRating = "(${restaurant.review_count})"
        )

        restaurant_details_tab_layout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val currentSelectedFragment =   when (tab?.position) {
                    0 -> restaurantCovidInfoFragment
                    1 -> reviewDetailsFragment
                    else -> reserveFragment
                }
                supportFragmentManager.apply {
                    fragments.forEach { beginTransaction().hide(it).commit() }
                        .also { beginTransaction().show(currentSelectedFragment).commit() }
                }
            }

        })


        setUiElements(restaurantUiModel,restaurantCovidInfoFragment)


    }

    private fun setUiElements(restaurantDetails: RestaurantUiModel,restaurantCovidInfoFragment:RestaurantCovidInfoFragment) {

        restaurantCovidInfoFragment.restaurantImageUrlListener = {

            it?.let {
                Glide.with(this@RestaurantDetailsActivity).load(it)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(restaurant_details_image)

            }
        }
        restaurant_details_name.text = restaurant.name
        restaurant_details_rating.rating = restaurantDetails.ratingText.toFloat()
        val spannable =
            Spannable.Factory.getInstance().newSpannable(restaurant_details_price_range.text)
                .apply {
                    setSpan(
                        ForegroundColorSpan(Color.parseColor("#EF5350")),
                        0,
                        restaurantDetails.priceRange.length,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                }
        restaurant_details_price_range.setText(spannable, TextView.BufferType.SPANNABLE)

        restaurant_details_phone_number.text = restaurantDetails.phoneNumbers


        restaurant_details_cuisine_type.text = restaurantDetails.cuisines
        restaurant_details_address.text = restaurantDetails.address
        restaurant_details_aggregate_rating.text = restaurantDetails.aggregateRating

        google_maps_button.setOnClickListener {
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("geo:42.323971,-83.202873?q=${Uri.parse(restaurant.location.address)}")
            ).also {
                startActivity(it)
            }
        }
    }

}

