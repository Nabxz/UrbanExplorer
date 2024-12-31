package com.example.urbanexplorer.ui.screens

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.urbanexplorer.R
import com.example.urbanexplorer.data.LocalLocationDataProvider
import com.example.urbanexplorer.data.Location
import com.example.urbanexplorer.data.LocationCategories

@Composable
fun DisplayFilters(
    tweakFilter: (LocationCategories) -> Unit,
    initialActiveFilters: Set<LocationCategories>,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(paddingValues)
    ) {
        items(LocationCategories.entries.toTypedArray()) { filter ->

            val isActive = initialActiveFilters.contains(filter)

            OutlinedButton(
                onClick = { tweakFilter(filter) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors().copy(
                    contentColor = if(isActive) {MaterialTheme.colorScheme.primary} else {Color.Gray}
                ),
            ) {
                Text(text = filter.name)
            }
        }
    }

}

@Composable
fun StaticSearchBar(
    onSearchBarClicked: () -> Unit,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(horizontal = 30.dp)
) {
    TextField(
        value = "",
        onValueChange = {},
        placeholder = { Text(text = "Search") },
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onSearchBarClicked()
                }
            },
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        ),
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
    )

}

@Composable
fun DisplayActivityExpanded(activity: Location, onActivityClicked: (Location) -> Unit) {

    Column(
        modifier = Modifier
            .height(240.dp)
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .clickable { onActivityClicked(activity) }
    ) {
        Image(
            painter = painterResource(id = activity.imagesIdList[0]),
            contentDescription = null,
            modifier = Modifier
                .height(170.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )

        // Name, Rating & Category
        Column(Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = activity.name)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Rounded.Star, contentDescription = "rating")
                    Text(text = activity.rating.toString() + "/5.0")
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = activity.categoryIcon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                    modifier = Modifier.height(20.dp)
                )
                Text(text = activity.category.name)
            }
        }
    }
}


@Composable
fun DisplayCityExplorerContent(
    onSearchBarClicked: () -> Unit,
    tweakFilter: (LocationCategories) -> Unit,
    initialActiveFilters: Set<LocationCategories>,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {

    Column(modifier = Modifier.padding(paddingValues)) {
        Text(
            text = stringResource(R.string.explore),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 6.dp)
        )

        StaticSearchBar(onSearchBarClicked, paddingValues = PaddingValues(6.dp))

        DisplayFilters(
            tweakFilter = tweakFilter,
            initialActiveFilters = initialActiveFilters,
            paddingValues = PaddingValues(top = 8.dp, bottom = 12.dp)
        )
    }

}

@Composable
fun DisplayListOfActivities(
    title: String = "Locations",
    cityActivities: List<Location>,
    listDisplay: @Composable (activity: Location) -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(paddingValues)
    ){

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 6.dp)
        )
        LazyRow() {
            items(cityActivities) { activity->
                listDisplay(activity)
            }
        }
    }
}

@Composable
fun DisplayActivity(activity: Location, onActivityClicked: (Location) -> Unit) {

    Box(
        Modifier
            .width(130.dp)
            .height(150.dp)
            .padding(5.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onActivityClicked(activity)
            }
    ) {
        Image(
            painter = painterResource(id = activity.imagesIdList[0]),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
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

        // Activity Name & Icon
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        ) {

            Text(
                text = activity.name,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = activity.categoryIcon),
                    contentDescription = null,
                    Modifier.height(14.dp)
                )
                Text(
                    text = activity.category.name,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 10.sp,

                    )
            }

        }

    }

}

@Composable
fun CircleComposable(
    @DrawableRes imageId: Int? = null,
    color: Color = Color.Transparent,
    size: Dp = 50.dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape = CircleShape)
            .background(color)
    ) {
        if (imageId != null) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview
@Composable
fun DisplayCounter(
    currentPosition: Int = 0,
    maxPosition: Int = 0,
    color: Color = MaterialTheme.colorScheme.secondaryContainer,
    roundedEdgeDp: Dp = 8.dp,
    paddingValues: PaddingValues = PaddingValues(4.dp)
) {
    Row(
        modifier = Modifier.background(color = color, shape = RoundedCornerShape(roundedEdgeDp)).padding(paddingValues)
    ) {
        Text(text = "$currentPosition/$maxPosition")
    }
}

@Preview()
@Composable
fun PreviewCircleComposable() {
    CircleComposable(color = Color.Black)
}

@Preview
@Composable
fun PreviewDisplayActivity() {
    DisplayActivity(activity = LocalLocationDataProvider.listOfLocations[0], onActivityClicked = {})
}

@Preview
@Composable
fun PreviewDisplayFilters() {
    DisplayFilters(
        tweakFilter = {},
        initialActiveFilters = LocationCategories.entries.toSet()
    )
}

@Preview
@Composable
fun PreviewDisplayCityExplorerContent() {
    DisplayCityExplorerContent(
        onSearchBarClicked = {},
        tweakFilter = {},
        initialActiveFilters = LocationCategories.entries.toSet(),
    )
}

@Preview
@Composable
fun PreviewDisplayActivityExpanded() {
    DisplayActivityExpanded(activity = LocalLocationDataProvider.listOfLocations[0], onActivityClicked = {})
}