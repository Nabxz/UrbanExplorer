package com.example.urbanexplorer.ui

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.urbanexplorer.data.LocalLocationDataProvider
import com.example.urbanexplorer.data.ScreenSizes
import com.example.urbanexplorer.ui.screens.DetailsScreen
import com.example.urbanexplorer.ui.screens.HomeScreen
import com.example.urbanexplorer.ui.screens.SearchScreen
import com.example.urbanexplorer.ui.screens.UrbanExplorerScreens


@Composable
fun UrbanExplorerApp(windowSize: WindowWidthSizeClass) {

    val activity = LocalContext.current as Activity

    // ViewModel & UiState
    val viewModel: AppViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Determine Phone/Tablet Mode
    val screenSize: ScreenSizes
    when(windowSize) {
        WindowWidthSizeClass.Compact -> {
            screenSize = ScreenSizes.COMPACT
        }
        else -> {
            screenSize = ScreenSizes.EXPANDED
        }
    }

    Scaffold { innerPadding ->

        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = UrbanExplorerScreens.HOME.name,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(route = UrbanExplorerScreens.HOME.name){

                // Phone
                if(screenSize == ScreenSizes.COMPACT) {
                    HomeScreen(
                        onSearchBarClicked = {navController.navigate(UrbanExplorerScreens.SEARCH.name)},
                        cityActivities = viewModel.currentCityActivities,
                        currentCity = viewModel.currentLocation,
                        screenSize = screenSize,
                        onActivityClicked = {
                            viewModel.updateDisplayedLocation(it)
                            navController.navigate(UrbanExplorerScreens.DETAILS.name)
                        },
                        tweakFilter = {viewModel.tweakFilter(it)},
                        initialActiveFilters = uiState.activeFilters,
                        contentPadding = innerPadding,
                    )

                // Tablet
                } else {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Column(Modifier.weight(1f)) {
                            HomeScreen(
                                onSearchBarClicked = {navController.navigate(UrbanExplorerScreens.SEARCH.name)},
                                cityActivities = viewModel.currentCityActivities,
                                currentCity = viewModel.currentLocation,
                                screenSize = screenSize,
                                onActivityClicked = {
                                    viewModel.updateDisplayedLocation(it)
                                },
                                tweakFilter = { viewModel.tweakFilter(it) },
                                initialActiveFilters = uiState.activeFilters,
                                contentPadding = innerPadding,
                            )
                        }

                        Column(Modifier.weight(0.9f)) {
                            DetailsScreen(
                                location = uiState.displayedLocation,
                                screenSize = screenSize,
                                cityActivities = viewModel.currentCityActivities,
                                onActivityClicked = { viewModel.updateDisplayedLocation(it) },
                                initialActiveFilters = uiState.activeFilters,
                                tweakFilter = { viewModel.tweakFilter(it) },
                                onSearchBarClicked = {navController.navigate(UrbanExplorerScreens.SEARCH.name)},
                                // If Screen Is In Tablet Mode Back should first remove location, if no location then close
                                backButtonPressedOnExpandedScreen = {
                                    if (uiState.displayedLocation != null) {
                                        viewModel.updateDisplayedLocation(null)
                                    } else {
                                        activity.finish()
                                    }
                                },
                                navigateBack = {},
                                displayedLocationImageCounter = uiState.displayedLocationImageCounter,
                                swipeImageLeft = {
                                    // Image Swiped Foward
                                    if(it) {
                                        viewModel.detailsPageNextImage()
                                        // Image Swiped Backward
                                    } else {
                                        viewModel.detailsPagePrevImage()
                                    }
                                },
                                contentPadding = innerPadding,
                            )
                        }

                    }

                }

            }

            composable(route = UrbanExplorerScreens.DETAILS.name) {

                DetailsScreen(
                    location = uiState.displayedLocation,
                    screenSize = screenSize,
                    cityActivities = viewModel.currentCityActivities,
                    onActivityClicked = { viewModel.updateDisplayedLocation(it) },
                    initialActiveFilters = uiState.activeFilters,
                    tweakFilter = { viewModel.tweakFilter(it) },
                    onSearchBarClicked = {navController.navigate(UrbanExplorerScreens.SEARCH.name)},
                    navigateBack = {
                        navController.navigateUp()
                        viewModel.updateDisplayedLocation(null)
                    },
                    swipeImageLeft = {
                        // Image Swiped Foward
                        if(it) {
                            viewModel.detailsPageNextImage()
                            // Image Swiped Backward
                        } else {
                            viewModel.detailsPagePrevImage()
                        }
                    },
                    displayedLocationImageCounter = uiState.displayedLocationImageCounter,
                    contentPadding = innerPadding,
                )
            }

            composable(route = UrbanExplorerScreens.SEARCH.name) {

                SearchScreen(
                    searchBarValue = viewModel.searchFieldTextInput,
                    onValueChanged = {viewModel.updateSearchField(it)},
                    cityActivities = viewModel.currentCityActivities,
                    navigateBack = {
                        viewModel.updateSearchField("")
                        navController.navigateUp()
                    },
                    screenSize = screenSize,
                    onActivityClicked = {
                        viewModel.updateDisplayedLocation(it)
                        viewModel.updateSearchField("")
                        if (screenSize == ScreenSizes.COMPACT) {
                            navController.navigate(UrbanExplorerScreens.DETAILS.name)
                        } else {
                            navController.navigate(UrbanExplorerScreens.HOME.name)
                        }

                    }
                )

            }
        }

    }


}