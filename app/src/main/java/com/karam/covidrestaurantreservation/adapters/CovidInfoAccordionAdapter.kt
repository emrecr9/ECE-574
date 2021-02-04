package com.karam.covidrestaurantreservation.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.karam.covidrestaurantreservation.R
import com.karam.covidrestaurantreservation.models.CovidInfoItem
import kotlinx.android.synthetic.main.covid_info_item.view.*
import kotlinx.android.synthetic.main.covid_precaution_list_item.view.*

class CovidInfoAccordionAdapter() : RecyclerView.Adapter<CovidInfoAccordionViewHolder>() {

    var covidInfoList: MutableList<CovidInfoItem> = mutableListOf()
        set(value) {
            field  = value
            notifyDataSetChanged()
        }

    private val linearInfoItem = LinearInterpolator()
    override fun onBindViewHolder(holder: CovidInfoAccordionViewHolder, position: Int) {

        holder.image.setImageResource(covidInfoList[position].image)
        holder.title.text = covidInfoList[position].title

        covidInfoList[position].sublist.forEach {
            val view = LayoutInflater.from(holder.itemView.context).inflate(R.layout.covid_precaution_list_item,null,false) as LinearLayout
            view.covid_bulletpoint_text.text = it
            holder.accordion.addView(view)
        }

        holder.itemView.setOnClickListener {
            if(holder.accordion.isVisible) {
                holder.accordion.visibility = View.GONE
            }
            else {
                holder.accordion.visibility = View.VISIBLE
            }
            holder.expandIcon.animate().rotationBy(180f).setInterpolator(linearInfoItem).setDuration(200).start()
        }
        if (covidInfoList[position].sublist.isEmpty()) {
            val view = LayoutInflater.from(holder.itemView.context).inflate(R.layout.covid_could_not_load_data_item,null,false) as ConstraintLayout
            holder.accordion.addView(view)
        }



    }
    override fun getItemCount() = covidInfoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CovidInfoAccordionViewHolder =
        CovidInfoAccordionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.covid_info_item,
                parent,
                false
            )
        )
}

class CovidInfoAccordionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val image = view.covid_info_item_image as ImageView
    val title = view.covid_info_item_title as TextView
    val expandIcon = view.caret_down_icon as ImageView
    val accordion = view.covid_info_accordion as LinearLayout
}