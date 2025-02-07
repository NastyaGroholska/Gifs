package com.ahrokholska.gifs.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage

@Composable
fun HomeScreen() {
    HomeScreenContent(
        gifs = List(15) { "https://media3.giphy.com/media/v1.Y2lkPWEwMDMxZWRkNXI1MXgzZ3g1dXZvZnplOXA1bHpza2Y1NG1yeGtnMHllZzBjNnFoZyZlcD12MV9naWZzX3NlYXJjaCZjdD1n/IgOEWPOgK6uVa/200w.gif" }
    )
}

@Composable
fun HomeScreenContent(gifs: List<String>) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchField(
                    modifier = Modifier.weight(1f),
                    onSearchChanged = {
                        //TODO
                    }
                )
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .clickable {
                            //TODO
                        }
                        .padding(5.dp),
                    imageVector = Icons.Outlined.Search,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null
                )
            }
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .padding(top = 10.dp),
                columns = StaggeredGridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalItemSpacing = 5.dp
            ) {
                items(gifs) {
                    SubcomposeAsyncImage(
                        modifier = Modifier.clip(RoundedCornerShape(5.dp)),
                        contentScale = ContentScale.FillWidth,
                        model = it,
                        loading = { CircularProgressIndicator() },
                        error = {
                            Icon(
                                painter = rememberVectorPainter(image = Icons.Filled.Clear),
                                contentDescription = null
                            )
                        },
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchField(modifier: Modifier = Modifier, onSearchChanged: (String) -> Unit) {
    var value by rememberSaveable { mutableStateOf("") }
    BasicTextField(
        modifier = modifier
            .background(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            )
            .padding(8.dp),
        value = value,
        onValueChange = {
            value = it
            onSearchChanged(it)
        },
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.CenterStart) {
                Text(
                    text = if (value.isEmpty()) "Search" else "",
                    color = MaterialTheme.colorScheme.secondary
                )
                innerTextField()
            }
        }
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreenContent(
        gifs = List(15) { "https://media3.giphy.com/media/v1.Y2lkPWEwMDMxZWRkNXI1MXgzZ3g1dXZvZnplOXA1bHpza2Y1NG1yeGtnMHllZzBjNnFoZyZlcD12MV9naWZzX3NlYXJjaCZjdD1n/IgOEWPOgK6uVa/200w.gif" }
    )
}

