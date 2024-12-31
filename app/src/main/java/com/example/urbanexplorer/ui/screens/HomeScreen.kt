package com.example.urbanexplorer.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.urbanexplorer.R
import com.example.urbanexplorer.data.CurrentCity
import com.example.urbanexplorer.data.LocalLocationDataProvider
import com.example.urbanexplorer.data.Location
import com.example.urbanexplorer.data.LocationCategories
import com.example.urbanexplorer.data.ScreenSizes
import com.example.urbanexplorer.ui.AppViewModel
import com.example.urbanexplorer.ui.theme.UrbanExplorerTheme

@Composable
fun HomeScreen(
    onSearchBarClicked: () -> Unit,
    currentCity: CurrentCity,
    cityActivities: List<Location>,
    onActivityClicked: (Location) -> Unit,
    tweakFilter: (LocationCategories) -> Unit,
    initialActiveFilters: Set<LocationCategories>,
    modifier: Modifier = Modifier,
    screenSize: ScreenSizes = ScreenSizes.COMPACT,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {

        item {
            // Change Location
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement =
                if(screenSize == ScreenSizes.COMPACT) { Arrangement.End } else { Arrangement.Start }
            ) {
                Text(text = stringResource(R.string.change_location_button))
            }

            // City Name
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.location),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = currentCity.name),
                    fontWeight = FontWeight.ExtraLight,
                    fontSize = 44.sp
                )
            }

            // Image & Temperature
            Box(modifier = Modifier.padding(bottom = 20.dp)) {
                Image(
                    painter = painterResource(id = currentCity.cityImageId),
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                )

                // Temperature
                Text(
                    text = currentCity.temperature.toString() + "°ᶜ",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraLight,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 10.dp, end = 20.dp)
                )
            }

            // Search Bar
            StaticSearchBar(onSearchBarClicked)


        }

        // Popular Activities & FunFact
        item {
            val paddingValues = PaddingValues(vertical = 14.dp, horizontal = 4.dp)
            if(screenSize == ScreenSizes.COMPACT) {

                DisplayListOfActivities(
                    title = "Popular",
                    cityActivities = cityActivities,
                    listDisplay = { activity ->
                        DisplayActivity(activity = activity, onActivityClicked = onActivityClicked)
                                  },
                    paddingValues = paddingValues
                )
            }

            DisplayFunFact(
                currentCity = currentCity,
                paddingValues = paddingValues
            )

            if (screenSize == ScreenSizes.COMPACT) {
                DisplayCityExplorerContent(
                    onSearchBarClicked = onSearchBarClicked,
                    tweakFilter = tweakFilter,
                    initialActiveFilters = initialActiveFilters,
                    paddingValues = paddingValues
                )
            }
        }

        // Get Filtered Locations & Display them
        var filteredCityActivities = cityActivities.filter { initialActiveFilters.contains(it.category) }
        if (filteredCityActivities.isEmpty()) {filteredCityActivities = cityActivities} // If no filters are selected display all
        items(filteredCityActivities) { activity ->
            if (screenSize == ScreenSizes.COMPACT) {
                DisplayActivityExpanded(activity = activity, onActivityClicked = onActivityClicked)
            }
        }

    }

}

@Composable
fun DisplayFunFact(
    currentCity: CurrentCity,
    paddingValues: PaddingValues
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(paddingValues)
        ) {
        Text(
            text = stringResource(R.string.fun_fact),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 6.dp)
        )
        Box() {
            Image(
                painter = painterResource(id = currentCity.funFactImageId),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.BottomEnd
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            )
                        )
                    )
            )
            Text(
                text = stringResource(id = currentCity.funFact),
                fontStyle = FontStyle.Italic,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
            )

        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    UrbanExplorerTheme(dynamicColor = false,  darkTheme = false) {
        Scaffold() { innerPadding ->
            HomeScreen(
                onSearchBarClicked = {},
                cityActivities = LocalLocationDataProvider.listOfLocations,
                currentCity = LocalLocationDataProvider.CurrentCity,
                screenSize = ScreenSizes.COMPACT,
                onActivityClicked = {},
                tweakFilter = {},
                initialActiveFilters = LocationCategories.entries.toSet(),
                contentPadding = innerPadding,
            )
        }
    }
}



@Preview
@Composable
fun PreviewFunFact() {
    DisplayFunFact(
        currentCity = LocalLocationDataProvider.CurrentCity,
        paddingValues = PaddingValues(vertical = 14.dp, horizontal = 4.dp)
    )
}


