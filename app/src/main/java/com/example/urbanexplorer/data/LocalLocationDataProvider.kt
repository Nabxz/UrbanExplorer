package com.example.urbanexplorer.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.urbanexplorer.R

object LocalLocationDataProvider {

    val CurrentCity = CurrentCity(
        name = R.string.currentCity,
        temperature = 16,
        funFact = R.string.currentCityFunFact,
        cityImageId = R.drawable.localcity,
        funFactImageId = R.drawable.localcity1
    )

    val listOfLocations = listOf(
        // Coffee
        Location(
            name = "Second Cup Coffee",
            category = LocationCategories.COFFEE,
            categoryIcon = R.drawable.coffee_icon,
            imagesIdList = listOf(R.drawable.coffeeshop1_1, R.drawable.coffeeshop1_2),
            rating = 4.8,
            description = R.string.coffee1_description
        ),
        Location(
            name = "Starbucks",
            category = LocationCategories.COFFEE,
            categoryIcon = R.drawable.coffee_icon,
            imagesIdList = listOf(R.drawable.coffeeshop2_1, R.drawable.coffeeshop2_2),
            rating = 4.9,
            description = R.string.coffee2_description
        ),
        Location(
            name = "Thom Bargen Coffee",
            category = LocationCategories.COFFEE,
            categoryIcon = R.drawable.coffee_icon,
            imagesIdList = listOf(R.drawable.coffeeshop3_1, R.drawable.coffeeshop3_2),
            rating = 4.7,
            description = R.string.coffee3_description
        ),

        // Restaurant
        Location(
            name = "Peasant Cookery",
            category = LocationCategories.RESTAURANT,
            categoryIcon = R.drawable.restaurant_icon,
            imagesIdList = listOf(R.drawable.restaurant1_1, R.drawable.restaurant1_2),
            rating = 4.8,
            description = R.string.restaurant1_description
        ),
        Location(
            name = "The Grove",
            category = LocationCategories.RESTAURANT,
            categoryIcon = R.drawable.restaurant_icon,
            imagesIdList = listOf(R.drawable.restaurant2_1, R.drawable.restaurant2_2),
            rating = 4.9,
            description = R.string.restaurant2_description
        ),
        Location(
            name = "The Capital",
            category = LocationCategories.RESTAURANT,
            categoryIcon = R.drawable.restaurant_icon,
            imagesIdList = listOf(R.drawable.restaurant3_1, R.drawable.restaurant3_2),
            rating = 4.7,
            description = R.string.restaurant3_description
        ),

        // Park
        Location(
            name = "St. Vital Park",
            category = LocationCategories.PARK,
            categoryIcon = R.drawable.park_icon,
            imagesIdList = listOf(R.drawable.park1_1, R.drawable.park1_2),
            rating = 4.9,
            description = R.string.park1_description
        ),
        Location(
            name = "King's Park",
            category = LocationCategories.PARK,
            categoryIcon = R.drawable.park_icon,
            imagesIdList = listOf(R.drawable.park2_1, R.drawable.park2_2),
            rating = 4.8,
            description = R.string.park2_description
        ),
        Location(
            name = "The Forks",
            category = LocationCategories.PARK,
            categoryIcon = R.drawable.park_icon,
            imagesIdList = listOf(R.drawable.park3_1, R.drawable.park3_2),
            rating = 4.8,
            description = R.string.park3_description
        ),

        // Shop
        Location(
            name = "CF Polo Park",
            category = LocationCategories.SHOP,
            categoryIcon = R.drawable.mall_icon,
            imagesIdList = listOf(R.drawable.store1_1, R.drawable.store1_2),
            rating = 4.8,
            description = R.string.shop1_description
        ),
        Location(
            name = "Outlet Collection Winnipeg",
            category = LocationCategories.SHOP,
            categoryIcon = R.drawable.mall_icon,
            imagesIdList = listOf(R.drawable.store2_1, R.drawable.store2_2),
            rating = 4.9,
            description = R.string.shop2_description
        ),
        Location(
            name = "St. Vital Centre",
            category = LocationCategories.SHOP,
            categoryIcon = R.drawable.mall_icon,
            imagesIdList = listOf(R.drawable.store3_1, R.drawable.store3_2),
            rating = 4.8,
            description = R.string.shop3_description
        )
    ) // End of listOfActivities


}

data class CurrentCity (
    @StringRes val name: Int,
    val temperature: Int,
    @StringRes val funFact: Int,
    @DrawableRes val cityImageId: Int,
    @DrawableRes val funFactImageId: Int
)