package com.karam.covidrestaurantreservation.adapters

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.karam.covidrestaurantreservation.R
import com.karam.covidrestaurantreservation.models.ReservationItem
import kotlinx.android.synthetic.main.circular_checkbox.view.*
import kotlinx.android.synthetic.main.covid_info_item.view.*
import kotlinx.android.synthetic.main.covid_precaution_list_item.view.*
import java.util.*

class ReservationItemAdapter() : RecyclerView.Adapter<StringViewHolder>() {


    var selectedItemIndex = NOT_SELECTED
    var selectedDate : Date = Calendar.getInstance().time
    var items: List<ReservationItem> = mutableListOf()
        set(value) {
            field  = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
        holder.text.text = items[position].title

        if(position != selectedItemIndex) holder.image.setBackgroundResource(R.drawable.circle)
        holder.image.setOnClickListener {
            if(holder.isSelected)
            {
                holder.isSelected = false
                it.setBackgroundResource(R.drawable.circle)
                notifyDataSetChanged()
            }
            else {
                selectedDate = items[position].date
                holder.isSelected = true
                it.setBackgroundResource(R.drawable.filledcircle)
                selectedItemIndex = position
                notifyDataSetChanged()

            }

        }

    }
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder =
        StringViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.circular_checkbox,
                parent,
                false
            )
        )
}

class StringViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val text = view.checkbox_text as TextView
    val image = view.checkbox_image as ImageView
    var isSelected = false
}

const val NOT_SELECTED = -1