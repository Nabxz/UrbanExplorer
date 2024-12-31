package com.example.urbanexplorer.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Location(
    val name: String,
    val category: LocationCategories,
    @DrawableRes
    val categoryIcon: Int,
    val imagesIdList: List<Int>,
    val rating: Double,
    @StringRes val description: Int
 )
