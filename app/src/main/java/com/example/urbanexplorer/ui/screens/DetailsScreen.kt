package com.example.urbanexplorer.ui.screens

import android.telecom.Call.Details
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.urbanexplorer.R
import com.example.urbanexplorer.data.LocalLocationDataProvider
import com.example.urbanexplorer.data.Location
import com.example.urbanexplorer.data.LocationCategories
import com.example.urbanexplorer.data.ScreenSizes
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailsScreen(
    location: Location?,
    screenSize: ScreenSizes = ScreenSizes.COMPACT,
    cityActivities: List<Location>,
    onActivityClicked: (Location) -> Unit,
    onSearchBarClicked: () -> Unit,
    tweakFilter: (LocationCategories) -> Unit,
    initialActiveFilters: Set<LocationCategories>,
    backButtonPressedOnExpandedScreen: () -> Unit = {},
    navigateBack: () -> Unit,
    displayedLocationImageCounter: Int = 0,
    swipeImageLeft: (Boolean) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    BackHandler(enabled = (screenSize == ScreenSizes.EXPANDED)) {
        backButtonPressedOnExpandedScreen()

    }

   Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
   ) {
        if (location != null) {
            Column {
                // Image, ImageCarousel And BackButton(for phones)
                val coroutineScope = rememberCoroutineScope()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures { change, dragAmount ->
                                if (dragAmount > 0) {
                                    // Swiped right
                                    coroutineScope.launch {
                                        Log.d(
                                            "TEST",
                                            "SWIPED RIGHT: imageCounter: $displayedLocationImageCounter , imagesIDListSize: ${location.imagesIdList.size}"
                                        )
                                        swipeImageLeft(false)
                                    }
                                } else if (dragAmount < 0) {
                                    // Swiped left
                                    coroutineScope.launch {
                                        swipeImageLeft(true)
                                    }
                                }
                                change.consume()
                            }
                        }
                )  {

                    // Background Image
                    Image(
                        painter = painterResource(id = location.imagesIdList[displayedLocationImageCounter]),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Backbutton(Visible for phones only)
                    if(screenSize == ScreenSizes.COMPACT) {
                        Row(modifier = Modifier.align(Alignment.TopStart)) {
                            IconButton(onClick = { navigateBack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                    }

                    // Bottom Circles
                    Row(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(location.imagesIdList.size) {
                            CircleComposable(
                                color = if (it == displayedLocationImageCounter) {MaterialTheme.colorScheme.primary} else {Color.LightGray},
                                size = 12.dp
                            )
                        }
                    }

                    // Current Image Counter
                    Row(
                        Modifier
                            .align(Alignment.BottomEnd)
                            .padding(5.dp)) {
                        DisplayCounter(
                            currentPosition = displayedLocationImageCounter + 1,
                            maxPosition = location.imagesIdList.size,
                            roundedEdgeDp = 12.dp
                        )
                    }
                }

                Column(
                    Modifier
                        .padding(10.dp)
                        .fillMaxHeight()) {

                    // Location name, category and rating
                    Text(
                        text = location.name,
                        fontWeight = FontWeight.ExtraLight,
                        fontSize = 44.sp
                    )
                    Text(text = location.category.name)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 14.dp, bottom = 8.dp)
                    ) {
                        Icon(imageVector = Icons.Rounded.Star, contentDescription = null)
                        Text(text = "${location.rating}/5.0  (${(location.rating * 124).toInt()} Reviews)")
                    }

                    // Scrollable Description
                    Text(
                        text = stringResource(R.string.description),
                        fontSize = 20.sp,
                    )
                    LazyColumn {
                        item {
                            Text(
                                text = stringResource(id = location.description),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }

                    // Get Location and Contact us buttons
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 12.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.get_location))
                        }
                        Text(
                            text = stringResource(R.string.contact_us),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }


            }

        // Only Visible For Tablets, When no location is being displayed
        } else if(screenSize == ScreenSizes.EXPANDED) {

            val paddingValues = PaddingValues(vertical = 14.dp, horizontal = 4.dp)
            LazyColumn {
                item {
                    DisplayListOfActivities(
                        title = "Popular",
                        cityActivities = cityActivities,
                        listDisplay = { activity ->
                            DisplayActivity(activity = activity, onActivityClicked = onActivityClicked)
                        },
                        paddingValues = paddingValues
                    )

                    DisplayCityExplorerContent(
                        onSearchBarClicked = onSearchBarClicked,
                        tweakFilter = tweakFilter,
                        initialActiveFilters = initialActiveFilters,
                        paddingValues = paddingValues
                    )

                }

                // Get Filtered Locations & Display them
                var filteredCityActivities = cityActivities.filter { initialActiveFilters.contains(it.category) }
                if (filteredCityActivities.isEmpty()) {filteredCityActivities = cityActivities} // If no filters are selected display all
                items(filteredCityActivities) { activity ->
                    DisplayActivityExpanded(activity = activity, onActivityClicked = onActivityClicked)
                }
            }
        }



   }

}

@Preview
@Composable
fun previewDetailsScreen() {
    DetailsScreen(
        location = LocalLocationDataProvider.listOfLocations[0],
        screenSize = ScreenSizes.COMPACT,
        cityActivities = LocalLocationDataProvider.listOfLocations,
        onActivityClicked = {},
        initialActiveFilters = LocationCategories.values().toSet(),
        onSearchBarClicked = {},
        tweakFilter = {},
        swipeImageLeft = {},
        navigateBack = {}
    )
}