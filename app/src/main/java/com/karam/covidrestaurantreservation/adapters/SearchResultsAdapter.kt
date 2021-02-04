package com.karam.covidrestaurantreservation.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.karam.covidrestaurantreservation.R
import com.karam.covidrestaurantreservation.models.ZomatoRestaurant
import com.karam.covidrestaurantreservation.models.ZomatoRestaurantDetails
import com.karam.covidrestaurantreservation.utils.Recommendation
import com.karam.covidrestaurantreservation.views.RestaurantDetailsActivity
import kotlinx.android.synthetic.main.search_result_item.view.*
import java.util.*

class SearchResultsAdapter(private val context: Context) : RecyclerView.Adapter<SearchResultsViewHolder>() {


    var restaurantList: List<ZomatoRestaurantDetails> = listOf()
        set(value) {
            field  = value
            notifyDataSetChanged()
        }
    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {

        holder.name.text = restaurantList[position].name
        holder.cuisine.text = restaurantList[position].cuisines
        holder.itemView.setOnClickListener {
            Intent(context,
                RestaurantDetailsActivity::class.java).apply {
                putExtra("restaurant",restaurantList[position])
                putExtra("covid_rate_info",restaurantList[position].covidCasesRateInfo)
                context.startActivity(this)
            }
        }
        holder.covidRate.apply {
            if(restaurantList[position].covidCasesRateInfo.covidCaseIncreasePast7Days > 200) {
                text = "High COVID rate"
                setTextColor(Color.RED)
            }
            else {
                text = "Low COVID rate"
                setTextColor(Color.GREEN)
            }
        }



        val cuisineList = restaurantList[position].cuisines.split(",").map { it.trim() }

       holder.cuisineImage.setImageResource(getCuisineImage(cuisineList[0]))


        val isSameAsUserPreference = cuisineList.filter { Recommendation.userCuisineSet.contains(it) }.isNotEmpty()
        if(Recommendation.userCuisineSet.isNotEmpty())
            if(cuisineList.filter { Recommendation.userCuisineSet.contains(it) }.size == 1)
            {
                holder.recommendedText.visibility = if(isSameAsUserPreference) View.VISIBLE else View.INVISIBLE
                holder.recommendedText.text = "Recommended!"
            }


        else if(cuisineList.filter { Recommendation.userCuisineSet.contains(it) }.size > 2) {
                holder.recommendedText.visibility = if(isSameAsUserPreference) View.VISIBLE else View.INVISIBLE
                holder.recommendedText.text = "Highly Recommended!!"

            }
    }

    override fun getItemCount() = restaurantList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder =
        SearchResultsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.search_result_item,
                parent,
                false
            )
        )
}

class SearchResultsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val name = view.restaurant_name as TextView
    val cuisine = view.restaurant_cuisine as TextView
    val cuisineImage = view.cuisine_image as ImageView
    val covidRate = view.covid_rate as TextView
    val recommendedText = view.recommended_label as TextView
    
}


fun getCuisineImage(cuisine:String) = when (cuisine) {
    "Mexican" -> R.drawable.ic_mexican_flag
    "American" -> R.drawable.ic_american_flag
    "BBQ" -> R.drawable.ic_bbq_icon
    "Sandwich" -> R.drawable.ic_sandwich
    "Burger" -> R.drawable.ic_burger
    "Vegetarian" -> R.drawable.ic_vegetarian
    else -> R.drawable.ic_italian_flag
}