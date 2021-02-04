package com.karam.covidrestaurantreservation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karam.covidrestaurantreservation.R
import com.karam.covidrestaurantreservation.models.Reservation
import kotlinx.android.synthetic.main.reservation_item.view.*
import java.text.SimpleDateFormat

class ReservationsAdapter() : RecyclerView.Adapter<ReservationViewHolder>() {

    var reservationList: List<Reservation> = listOf()
        set(value) {
            field  = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.reservationName.text = reservationList[position].restaurantName
        holder.reservationNumberOfPeople.text = reservationList[position].numberOfPeople
        holder.reservationDate.text = reservationList[position].reservationTimestamp.time.let { SimpleDateFormat("dd - MMM - YYYY").format(it)  }
        holder.reservationTime.text = reservationList[position].reservationTimestamp.time.time.let { SimpleDateFormat("hh:mm a").format(it) }
    }
    override fun getItemCount() = reservationList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder =
        ReservationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.reservation_item,
                parent,
                false
            )
        )
}

class ReservationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val reservationName = view.reservation_name as TextView
    val reservationNumberOfPeople = view.reservation_number_of_people as TextView
    val reservationDate = view.reservation_date as TextView
    val reservationTime = view.reservation_time as TextView
}