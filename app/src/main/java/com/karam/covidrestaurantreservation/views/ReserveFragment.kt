package com.karam.covidrestaurantreservation.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.karam.covidrestaurantreservation.R
import com.karam.covidrestaurantreservation.adapters.NOT_SELECTED
import com.karam.covidrestaurantreservation.adapters.ReservationItemAdapter
import com.karam.covidrestaurantreservation.models.Reservation
import com.karam.covidrestaurantreservation.models.ZomatoRestaurantDetails
import com.karam.covidrestaurantreservation.utils.Recommendation
import com.karam.covidrestaurantreservation.utils.Reservations
import kotlinx.android.synthetic.main.fragment_reserve.*
import kotlinx.android.synthetic.main.fragment_reserve.view.*
import java.util.*


class ReserveFragment : Fragment() {

    private lateinit var viewModel:ReserveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ReserveViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reserve, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val restaurant = arguments?.getParcelable("restaurant") as ZomatoRestaurantDetails?

        view.number_of_people_recyclerview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ReservationItemAdapter().also {
                it.items = viewModel.getNumberOfPeopleList()
            }

        }

        view.available_date_recyclerview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ReservationItemAdapter().also { it.items = viewModel.getAvailableDatesList() }

        }

        view.available_times_recyclerview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ReservationItemAdapter().also { it.items = viewModel.getAvailableTimesList()
            }
        }

        restaurant?.let { rest ->
            view.fragment_reserve_main_layout.visibility =
                if (rest.highlights.contains("Table booking recommended")) View.VISIBLE else View.GONE
            view.cannot_reserve_textview.visibility =
                if (rest.highlights.contains("Table booking recommended")) View.GONE else View.VISIBLE

            reserve_now_button.setOnClickListener {

                val numberOfPeopleAdapter =
                    view.number_of_people_recyclerview.adapter as ReservationItemAdapter
                val availableDateAdapter =
                    view.available_date_recyclerview.adapter as ReservationItemAdapter
                val availableTimeAdapter =
                    view.available_times_recyclerview.adapter as ReservationItemAdapter

                rest.cuisines.split(",")
                    .map { it.trim() }.forEach { Recommendation.userCuisineSet.add(it) }

                if(numberOfPeopleAdapter.selectedItemIndex == NOT_SELECTED)
                    Toast.makeText(requireContext(),"Please select the number of people",Toast.LENGTH_SHORT).show()
                else if (availableDateAdapter.selectedItemIndex == NOT_SELECTED)
                    Toast.makeText(requireContext(),"Please select the date of the reservation",Toast.LENGTH_SHORT).show()
                else if (availableTimeAdapter.selectedItemIndex == NOT_SELECTED)
                    Toast.makeText(requireContext(),"Please select time of the reservation",Toast.LENGTH_SHORT).show()
                else {
                    val numberOfPeople = numberOfPeopleAdapter.selectedItemIndex+1
                    val selectedDate = availableDateAdapter.items[availableDateAdapter.selectedItemIndex].date
                    val selectedTime = availableTimeAdapter.items[availableTimeAdapter.selectedItemIndex].title

                    Reservations.reservationList.add(
                        Reservation(
                            rest.name,
                            numberOfPeople.toString(),
                            getTimestamp(selectedDate,selectedTime)
                        )
                    )
                    Toast.makeText(requireContext(),"Reservation successful!",Toast.LENGTH_SHORT).show()
                    Intent(requireContext(), HomeActivity::class.java).also {
                        requireActivity().finish()
                        startActivity(it)
                    }
                }

            }

        }
    }


    companion object {
        @JvmStatic
        fun newInstance(restaurant: ZomatoRestaurantDetails) =
            ReserveFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("restaurant",restaurant)
                }
            }
    }

private fun getTimestamp(date: Date,timeOfDay:String): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = date
    val timeString = timeOfDay.split(" ")[0]
    val amOrPM = timeOfDay.split(" ")[1]

    calendar.set(Calendar.HOUR,timeString.split(":")[0].toInt())
    calendar.set(Calendar.MINUTE,timeString.split(":")[1].toInt())
    calendar.set(Calendar.AM_PM,if(amOrPM == "AM") Calendar.AM else Calendar.PM)

    return calendar
}


}