package com.ahrokholska.gifs.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.SubcomposeAsyncImage
import com.ahrokholska.gifs.R
import com.ahrokholska.gifs.domain.model.Gif
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    HomeScreenContent(
        gifs = viewModel.gifs.collectAsLazyPagingItems(),
        onSearchClick = viewModel::searchChanged,
    )
}

@Composable
fun HomeScreenContent(gifs: LazyPagingItems<Gif>, onSearchClick: (String) -> Unit = {}) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                var value by rememberSaveable { mutableStateOf("") }
                SearchField(
                    modifier = Modifier.weight(1f),
                    value = value,
                    onSearchChanged = { value = it }
                )
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .clickable {
                            onSearchClick(value)
                        }
                        .padding(5.dp),
                    imageVector = Icons.Outlined.Search,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null
                )
            }
            Box(contentAlignment = Alignment.Center) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                        .padding(top = 10.dp),
                    columns = StaggeredGridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalItemSpacing = 5.dp
                ) {
                    items(
                        count = gifs.itemCount,
                        key = { ind -> gifs[ind]?.id.toString() }
                    ) { index ->
                        SubcomposeAsyncImage(
                            modifier = Modifier.clip(RoundedCornerShape(5.dp)),
                            contentScale = ContentScale.FillWidth,
                            model = gifs[index]?.url,
                            loading = {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            },
                            error = {
                                Icon(
                                    painter = rememberVectorPainter(image = Icons.Filled.Clear),
                                    contentDescription = null
                                )
                            },
                            contentDescription = null,
                        )
                    }
                    if (gifs.loadState.append == LoadState.Loading) {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
                if (gifs.loadState.refresh == LoadState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchField(
    modifier: Modifier = Modifier,
    value: String,
    onSearchChanged: (String) -> Unit
) {
    BasicTextField(
        modifier = modifier
            .background(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            )
            .padding(8.dp),
        value = value,
        onValueChange = { onSearchChanged(it) },
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.CenterStart) {
                Text(
                    text = if (value.isEmpty()) stringResource(R.string.search) else "",
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
        gifs =
        flowOf(
            PagingData.from(
                List(15) {
                    Gif(
                        id = "",
                        url = "https://media3.giphy.com/media/v1.Y2lkPWEwMDMxZWRkNXI1MXgzZ3g1dXZvZnplOXA1bHpza2Y1NG1yeGtnMHllZzBjNnFoZyZlcD12MV9naWZzX3NlYXJjaCZjdD1n/IgOEWPOgK6uVa/200w.gif"
                    )
                }
            )).collectAsLazyPagingItems()
    )
}

