package com.karam.covidrestaurantreservation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karam.covidrestaurantreservation.R
import com.karam.covidrestaurantreservation.models.Review
import kotlinx.android.synthetic.main.review_item.view.*

class ReviewsAdapter() : RecyclerView.Adapter<ReviewViewHolder>() {

    var reviewList: List<Review> = listOf()
        set(value) {
            field  = value
            notifyDataSetChanged()
        }
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.reviewerUsername.text = reviewList[position].review.reviewer.reviewer_username
        holder.reviewText.text = reviewList[position].review.review_text
        holder.reviewRating.rating = reviewList[position].review.rating.toFloat()
    }
    override fun getItemCount() = reviewList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder =
        ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.review_item,
                parent,
                false
            )
        )
}

class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val reviewerUsername = view.reviewer_username as TextView
    val reviewText = view.reviewer_text as TextView
    val reviewRating = view.review_rating as RatingBar
}