package com.karam.covidrestaurantreservation.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.google.android.libraries.places.api.model.Place
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

data class OpenTableRestaurantResponse(
    @SerializedName("restaurants")
    val restarauntList: List<OpentableRestaurant>
)

data class ZomatoRestaurantResponse(
    @SerializedName("restaurants")
    val restarauntList: List<ZomatoRestaurant>
)

data class ZomatoLocationResponse(
    @SerializedName("location_suggestions")
    val locationList: List<ZomatoLocation>
)

data class ZomatoLocation(
    @SerializedName("entity_id")
    val entityId: String,
    @SerializedName("entity_type")
    val entityType: String
)


@Parcelize
data class ZomatoRestaurant(
    @SerializedName("restaurant") val restaurant: ZomatoRestaurantDetails
) : Parcelable

@Parcelize
data class AdditionalRestaurantInfo(
    val covidPrecautions: CovidPrecautions,
    val restaurantImageUrl : String?
):Parcelable
@Parcelize
data class CovidPrecautions(
    val cleaningAndSanitizingList: List<String>,
    val physicalDistancingList: List<String>,
    val personalProtectiveEquipmentList: List<String>,
    val screeningList:List<String>
) : Parcelable

@Parcelize
data class RestaurantUiModel(
    val restaurantName: String,
    val ratingText: String,
    val priceRange: String,
    val reviewCount: String,
    val phoneNumbers: String,
    val cuisines: String,
    val restaurantId: String,
    val address: String,
    val aggregateRating: String
) : Parcelable

@Parcelize
data class ZomatoRestaurantDetails(
    @SerializedName("name")
    val name: String,

    @SerializedName("price_range")
    val priceRange: String,

    @SerializedName("phone_numbers")
    val phoneNumbers: String,

    @SerializedName("cuisines")
    val cuisines: String,

    @SerializedName("user_rating")
    val rating: UserRating,

    @SerializedName("all_reviews_count")
    val review_count: String,

    @SerializedName("location")
    val location: Location,

    @SerializedName("id")
    val restaurantId: String,

    @SerializedName("highlights")
    val highlights: List<String>,

    var covidCasesRateInfo: CovidCasesRateInfo


) : Parcelable

@Parcelize
data class CovidCasesRateInfo(
    var covidCaseIncreasePast7Days: Int,

    var totalCovidCases:Int,

    var covidDeathIncreasePast7Days:Int,

    var totalCovidDeaths: Int
) :Parcelable

@Parcelize
data class OpentableRestaurant(

    @SerializedName("id")
    val id: String

) : Parcelable

@Parcelize
data class UserRating(
    @SerializedName("aggregate_rating")
    val aggregateRating: String
) : Parcelable

@Parcelize
data class Location(

    @SerializedName("address")
    val address: String,

    @SerializedName("zipcode")
    val zipCode: String
) : Parcelable

data class CovidResponse(
    @SerializedName("zipCd")
    val zipCode: String,
    @SerializedName("counties")
    val counties: List<Counties>
)

data class Counties(
    @SerializedName("historicData")
    val historicData: List<HistoricData>
)

data class HistoricData(
    @SerializedName("positiveCt")
    val positiveCasesCount: String,
    @SerializedName("deathCt")
    val deathCount: String,
    @SerializedName("date")
    val date: String
)

data class ReviewResponse(
    @SerializedName("user_reviews")
    val userReviews: List<Review>
)

data class Review(
    @SerializedName("review")
    val review: ReviewDetails
)

data class ReviewDetails(
    @SerializedName("rating")
    val rating: String,
    @SerializedName("user")
    val reviewer: Reviewer,
    @SerializedName("review_text")
    val review_text: String
)

data class Reviewer(
    @SerializedName("name")
    val reviewer_username: String
)

data class Reservation(
    val restaurantName: String,
    val numberOfPeople: String,
    val reservationTimestamp: Calendar
)

data class CovidInfoItem(
    @DrawableRes val image: Int,
    val title: String,
    val sublist: List<String>
)

data class ReservationItem(
    val title:String,
    val date:Date = Calendar.getInstance().time
)