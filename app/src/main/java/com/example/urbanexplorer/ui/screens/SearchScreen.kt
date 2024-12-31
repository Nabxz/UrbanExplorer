package com.example.urbanexplorer.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.urbanexplorer.data.LocalLocationDataProvider
import com.example.urbanexplorer.data.Location
import com.example.urbanexplorer.data.LocationCategories
import com.example.urbanexplorer.data.ScreenSizes

@Composable
fun SearchScreen(
    onValueChanged: (String) -> Unit,
    cityActivities: List<Location>,
    onActivityClicked: (Location) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    screenSize: ScreenSizes = ScreenSizes.COMPACT,
    searchBarValue: String = "",
    paddingValues: PaddingValues = PaddingValues(0.dp),
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    // Request focus on the TextField when the screen is first composed
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show() // Show the keyboard programmatically
    }

    // Current Filter, If Null then displays All
    var activeFilter:LocationCategories? by remember {
        mutableStateOf(null)
    }

    // Search Header
    Column(Modifier.padding(8.dp)) {

        TextField(
            value = searchBarValue,
            onValueChange = onValueChanged,
            placeholder = { Text(text = "Search") },
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .focusRequester(focusRequester),
            colors = TextFieldDefaults.colors().copy(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            leadingIcon = {
                if(screenSize == ScreenSizes.COMPACT) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back Button",
                        modifier = Modifier.clickable { navigateBack() }
                    )
                } else {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            )
        )

        // Filter
        LazyRow(Modifier.padding(top = 24.dp)) {

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .width(intrinsicSize = IntrinsicSize.Max)
                        .clickable { activeFilter = null }
                ) {
                    Text(text = "ALL")
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .height(7.dp)
                            .fillMaxWidth()
                            .background(
                                if (activeFilter == null) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    Color.Transparent
                                }
                            )
                    )
                }
            }

            items(LocationCategories.entries.toTypedArray()) { filter ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .width(intrinsicSize = IntrinsicSize.Max)
                        .clickable { activeFilter = filter }
                ) {
                    Text(text = filter.name)
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .height(7.dp)
                            .fillMaxWidth()
                            .background(
                                if (activeFilter == filter) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    Color.Transparent
                                }
                            )
                    )
                }
            }
        }

        // Line
        Box(
            modifier = Modifier
                .offset(y = -8.dp)
                .zIndex(-1f)
                .padding(top = 4.dp)
                .height(2.dp)
                .fillMaxWidth()
                .background(Color.Gray)

        )


        // Search Results
        LazyColumn(Modifier.padding(vertical = 8.dp)) {

            // Search Results
            // Get Filtered Locations & Display them
            var filteredCityActivities: List<Location>
            if (activeFilter != null) {
                filteredCityActivities = cityActivities.filter { activeFilter == it.category }
            // If no filters are selected display all
            } else {
                filteredCityActivities = cityActivities
            }

            // Filter Locations By SearchText
            if (searchBarValue.isNotBlank()) {
                filteredCityActivities = filteredCityActivities.filter { it.name.contains(searchBarValue, ignoreCase = true) }
            }
            items(filteredCityActivities) { activity ->

                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                        .clickable { onActivityClicked(activity) }
                ) {
                    SearchScreenActivityDisplay(activity = activity)
                }
                HorizontalDivider(thickness = 0.5.dp)
            }
        }
    }

}

@Composable
fun SearchScreenActivityDisplay(activity: Location) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircleComposable(imageId = activity.imagesIdList[0])
        Column {
            Text(
                text = activity.name,
                fontWeight = FontWeight.ExtraLight,
                fontSize = 20.sp
            )
            Text(
                text = activity.category.name,
                fontWeight = FontWeight.ExtraLight,
                fontSize = 12.sp
            )
        }
    }
}

@Preview
@Composable
fun PreviewSearchScreen() {
    SearchScreen(
        onValueChanged = {},
        cityActivities = LocalLocationDataProvider.listOfLocations,
        onActivityClicked = {},
        navigateBack = {},
    )
}

@Preview
@Composable
fun PreviewSearchScreenActivityDisplay() {
    SearchScreenActivityDisplay(activity = LocalLocationDataProvider.listOfLocations[0])
}