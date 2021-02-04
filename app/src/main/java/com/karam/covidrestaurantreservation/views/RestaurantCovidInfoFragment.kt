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
import com.karam.covidrestaurantreservation.adapters.CovidInfoAccordionAdapter
import com.karam.covidrestaurantreservation.models.CovidCasesRateInfo
import com.karam.covidrestaurantreservation.models.CovidInfoItem
import com.karam.covidrestaurantreservation.models.ZomatoRestaurantDetails
import com.karam.covidrestaurantreservation.services.OpenTableAdditionInfoService
import com.karam.covidrestaurantreservation.services.OpenTableService
import com.karam.covidrestaurantreservation.utils.RetrofitBuilder
import com.karam.covidrestaurantreservation.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_restaurant_covid_info.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class RestaurantCovidInfoFragment : Fragment() {

    private lateinit var covidInfoAccordionAdapter: CovidInfoAccordionAdapter
    private var covidCasesRateInfo: CovidCasesRateInfo? = null
    private var zomatoRestaurant: ZomatoRestaurantDetails? = null

    var restaurantImageUrlListener: ((String?) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        covidInfoAccordionAdapter = CovidInfoAccordionAdapter()
        covidCasesRateInfo = arguments?.getParcelable("covid_rate_info")
        zomatoRestaurant = arguments?.getParcelable("zomato_restaurant") as ZomatoRestaurantDetails?

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant_covid_info, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            restaurant: ZomatoRestaurantDetails,
            covidCasesRateInfo: CovidCasesRateInfo
        ) =
            RestaurantCovidInfoFragment().apply {
                arguments =
                    Bundle().also {
                        it.putParcelable("zomato_restaurant", restaurant)
                        it.putParcelable("covid_rate_info", covidCasesRateInfo)
                    }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel : RestaurantCovidInfoViewModel by viewModels {
            ViewModelFactory(
                RetrofitBuilder.
                provideRetrofit("http://opentable.herokuapp.com/api/")
                .create(OpenTableService::class.java),
                Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl("https://www.opentable.com/")
                    .build()
                    .create(OpenTableAdditionInfoService::class.java))
        }

        lifecycle.addObserver(viewModel)

        view.covid_info_recyclerview.apply {
            adapter = covidInfoAccordionAdapter
            layoutManager = LinearLayoutManager(context)
        }

        view.covid_fragment_loading_spinner.visibility = View.VISIBLE
        view.covid_fragment_main_layout.visibility = View.GONE

        covidCasesRateInfo?.let {
            covidInfoAccordionAdapter.covidInfoList.add(
                CovidInfoItem(
                    R.drawable.ic_covid_rate,
                    "Covid rate in the area",
                    listOf(
                        "Total number of COVID cases: ${it.totalCovidCases}",
                        "Total number of COVID deaths: ${it.totalCovidDeaths}",
                        "Covid case increase in the past 7 days: ${it.covidCaseIncreasePast7Days}",
                        "Covid death increase in the past 7 days: ${it.covidDeathIncreasePast7Days}"
                    )
                )
            )
        }


        lifecycleScope.launch(Dispatchers.Main) {

            val additionalRestaurantInfo =
                async { viewModel.getAdditionalRestaurantInfo(zomatoRestaurant) }

            additionalRestaurantInfo.await().let { additionalRestaurantInfo ->

                covidInfoAccordionAdapter.covidInfoList.add(
                    CovidInfoItem(
                        R.drawable.ic_cleaning_and_sanitizing,
                        "Covid Precautions: Cleaning and Sanitizing",
                        additionalRestaurantInfo.covidPrecautions.cleaningAndSanitizingList
                    )
                )

                covidInfoAccordionAdapter.covidInfoList.add(
                    CovidInfoItem(
                        R.drawable.ic_social_distancing, "Covid Precautions: Social Distancing",
                        additionalRestaurantInfo.covidPrecautions.physicalDistancingList
                    )
                )

                covidInfoAccordionAdapter.covidInfoList.add(
                    CovidInfoItem(
                        R.drawable.ic_face_mask, "Covid Precautions: Personal Protective Equipment",
                        additionalRestaurantInfo.covidPrecautions.personalProtectiveEquipmentList
                    )
                )

                covidInfoAccordionAdapter.covidInfoList.add(
                    CovidInfoItem(
                        R.drawable.ic_face_mask, "Covid Precautions: Screening",
                        additionalRestaurantInfo.covidPrecautions.screeningList
                    )
                )

                restaurantImageUrlListener?.invoke(additionalRestaurantInfo.restaurantImageUrl)
            }


            view.covid_fragment_loading_spinner.visibility = View.GONE
            view.covid_fragment_main_layout.visibility = View.VISIBLE
        }

    }

}
