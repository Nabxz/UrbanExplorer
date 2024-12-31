package com.example.urbanexplorer.ui

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.example.urbanexplorer.data.LocalLocationDataProvider
import com.example.urbanexplorer.data.Location
import com.example.urbanexplorer.data.LocationCategories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UrbanExplorerUiState())
    val uiState: StateFlow<UrbanExplorerUiState> = _uiState.asStateFlow()

    // Current City
    var currentLocation = LocalLocationDataProvider.CurrentCity
        private set

    // Current City Activities
    val currentCityActivities = LocalLocationDataProvider.listOfLocations

    // SearchScreen Search Bar
    var searchFieldTextInput by mutableStateOf("")
        private set

    fun updateSearchField(input: String) {
        searchFieldTextInput = input
    }

    // Display Activity Information
    fun updateDisplayedLocation(location: Location?) {
        // Remove Current Location
        if (location == null) {
            _uiState.update {
                it.copy(
                    displayedLocation = null,
                    displayedLocationImageCounter = 0
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    displayedLocation = location,
                    displayedLocationImageCounter = 0
                )
            }
        }
    }

    // Filters
    fun tweakFilter(filter: LocationCategories) {
        val updatedActiveFilters = _uiState.value.activeFilters.toMutableSet()
        if (updatedActiveFilters.contains(filter)) {
            updatedActiveFilters.remove(filter)
        } else {
            updatedActiveFilters.add(filter)
        }
        _uiState.update { it.copy(activeFilters = updatedActiveFilters) }
    }

    // Details Page Image
    fun detailsPageNextImage() {
        if(_uiState.value.displayedLocation != null) {
            _uiState.update { currentState ->
                val newCounter = currentState.displayedLocationImageCounter.inc()
                val maxIndex =
                    (currentState.displayedLocation?.imagesIdList?.size ?: (newCounter + 1)) - 1
                if (newCounter <= maxIndex) {
                    currentState.copy(displayedLocationImageCounter = newCounter)
                } else {
                    currentState // No change if newCounter is out of bounds
                }
            }
        }
    }


    fun detailsPagePrevImage() {
        if (_uiState.value.displayedLocation != null) {
            _uiState.update { currentState ->
                val newCounter = currentState.displayedLocationImageCounter.dec()
                if (newCounter >= 0) {
                    currentState.copy(displayedLocationImageCounter = newCounter)
                } else {
                    currentState // No change if newCounter is out of bounds
                }
            }
        }
    }



}

// UI State
data class UrbanExplorerUiState(

    // Filters
    val activeFilters: MutableSet<LocationCategories> = mutableSetOf(),

    // Details Screen
    val displayedLocation: Location? = null,
    val displayedLocationImageCounter: Int = 0
)
