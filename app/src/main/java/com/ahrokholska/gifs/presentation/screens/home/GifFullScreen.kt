package com.ahrokholska.gifs.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.SubcomposeAsyncImage
import com.ahrokholska.gifs.domain.model.Gif
import kotlinx.coroutines.flow.flowOf

@Composable
fun GifFullScreen(initialPosition: Int, viewModel: HomeScreenViewModel = hiltViewModel()) {
    GifFullScreenContent(
        initialPosition = initialPosition,
        gifs = viewModel.gifs.collectAsLazyPagingItems(),
        onDeleteImageClick = viewModel::deleteGif,
    )
}

@Composable
fun GifFullScreenContent(
    initialPosition: Int,
    gifs: LazyPagingItems<Gif>,
    onDeleteImageClick: (id: String) -> Unit = {}
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val pagerState = rememberPagerState(
            initialPage = initialPosition,
            pageCount = { gifs.itemCount }
        )
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding),
            state = pagerState,
            key = gifs.itemKey { it.id }
        ) { page ->
            gifs[page]?.let { gif ->
                val model by rememberSaveable(key = gif.id) { mutableStateOf(gif.url) }
                Box(contentAlignment = Alignment.BottomEnd) {
                    SubcomposeAsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                        model = model,
                        loading = {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize(Alignment.Center)
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
                    FloatingActionButton(
                        modifier = Modifier.padding(10.dp),
                        containerColor = MaterialTheme.colorScheme.error,
                        onClick = { onDeleteImageClick(gif.id) }
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Filled.Delete),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun GifFullScreenPreview() {
    GifFullScreenContent(
        initialPosition = 0,
        gifs = flowOf(PagingData.from(
            List(15) {
                Gif(
                    id = "",
                    url = "https://media3.giphy.com/media/v1.Y2lkPWEwMDMxZWRkNXI1MXgzZ3g1dXZvZnplOXA1bHpza2Y1NG1yeGtnMHllZzBjNnFoZyZlcD12MV9naWZzX3NlYXJjaCZjdD1n/IgOEWPOgK6uVa/200w.gif"
                )
            }
        )).collectAsLazyPagingItems())
}