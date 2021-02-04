package com.karam.covidrestaurantreservation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.karam.covidrestaurantreservation.R
import com.karam.covidrestaurantreservation.adapters.ReservationsAdapter
import kotlinx.android.synthetic.main.fragment_reservation_history.view.*


class ReservationHistoryFragment : Fragment() {

    private lateinit var viewModel:ReservationHistoryViewModel
    private lateinit var reservationsAdapter: ReservationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ReservationHistoryViewModel::class.java)
        reservationsAdapter = ReservationsAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reservation_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.reservations_history_recyclerview.apply {
            adapter = reservationsAdapter
            layoutManager = LinearLayoutManager(context)
        }

        reservationsAdapter.reservationList = viewModel.getUpcomingReservationList()
        showReservationsIfUpcomingListIsNotEmpty(view, reservationsAdapter)

        view.reservation_history_tab_layout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position) {
                    0 -> {
                        showReservationsIfUpcomingListIsNotEmpty(view, reservationsAdapter)
                    }
                    1 -> {
                        showReservationsIfPastReservationListIsNotEmpty(view, reservationsAdapter)
                    }
                }
            }

        })
    }

    private fun showReservationsIfPastReservationListIsNotEmpty(
        view: View,
        reservationsAdapter: ReservationsAdapter
    ) {

        if (viewModel.getPastReservationList().isEmpty()) {
            view.reservations_history_recyclerview.visibility = View.GONE
            view.no_reservations.visibility = View.VISIBLE
            view.no_reservations.text = "No Past Reservations"
        } else {
            view.reservations_history_recyclerview.visibility = View.VISIBLE
            view.no_reservations.visibility = View.GONE
            reservationsAdapter.reservationList = viewModel.getPastReservationList()
        }
    }

    private fun showReservationsIfUpcomingListIsNotEmpty(
        view: View,
        reservationsAdapter: ReservationsAdapter
    ) {

        if (viewModel.getUpcomingReservationList().isEmpty()) {
            view.reservations_history_recyclerview.visibility = View.GONE
            view.no_reservations.visibility = View.VISIBLE
            view.no_reservations.text = "No Upcoming Reservations"
        } else {
            view.reservations_history_recyclerview.visibility = View.VISIBLE
            view.no_reservations.visibility = View.GONE
            reservationsAdapter.reservationList = viewModel.getUpcomingReservationList()
        }
    }


}