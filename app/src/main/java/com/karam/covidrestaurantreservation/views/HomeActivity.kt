package com.karam.covidrestaurantreservation.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.karam.covidrestaurantreservation.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val searchFragment = SearchFragment()
        val reservationsFragment = ReservationHistoryFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, searchFragment,"search_fragment").commit()


        bottom_navigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.search_item -> {
                    supportFragmentManager.apply {
                        findFragmentByTag("reservations_history_fragment")?.let {
                            findFragmentByTag("search_fragment")?.let  {
                                beginTransaction().hide(reservationsFragment).show(searchFragment).commit()
                            }
                        }
                    }
                    true
                }
                R.id.reservations_item -> {
                    supportFragmentManager.apply {
                        findFragmentByTag("reservations_history_fragment")?.let {
                            findFragmentByTag("search_fragment")?.let  {
                                beginTransaction().hide(searchFragment).show(reservationsFragment).commit()
                            }
                        }?:beginTransaction().hide(searchFragment).add(R.id.fragment_container,reservationsFragment,"reservations_history_fragment").commit()
                    }
                    true
                }
                else -> false

            }

        }

    }


}