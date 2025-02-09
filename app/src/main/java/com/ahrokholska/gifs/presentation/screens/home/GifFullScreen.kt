package com.ahrokholska.gifs.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.SubcomposeAsyncImage
import com.ahrokholska.gifs.R
import com.ahrokholska.gifs.domain.model.Gif
import com.ahrokholska.gifs.presentation.common.AlertDialog
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GifFullScreen(
    initialPosition: Int,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    var openAlertDialog by remember { mutableStateOf(false) }
    var gifId by remember { mutableStateOf("") }
    if (openAlertDialog) {
        AlertDialog(
            onDismissRequest = { openAlertDialog = false },
            title = stringResource(R.string.are_you_sure_you_want_to_delete_this_gif_this_action_is_irreversible),
            onConfirmation = {
                openAlertDialog = false
                viewModel.deleteGif(gifId)
            }
        )
    }
    GifFullScreenContent(
        initialPosition = initialPosition,
        gifs = viewModel.gifs.collectAsLazyPagingItems(),
        onDeleteImageClick = {
            gifId = it
            openAlertDialog = true
        },
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GifFullScreenContent(
    initialPosition: Int,
    gifs: LazyPagingItems<Gif>,
    onDeleteImageClick: (id: String) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
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
                        modifier = with(sharedTransitionScope) {
                            Modifier
                                .fillMaxSize()
                                .sharedElement(
                                    rememberSharedContentState(key = gif.id),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                        },
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun GifFullScreenPreview() {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            GifFullScreenContent(
                initialPosition = 0,
                gifs = flowOf(PagingData.from(
                    List(15) {
                        Gif(
                            id = "",
                            url = "https://media3.giphy.com/media/v1.Y2lkPWEwMDMxZWRkNXI1MXgzZ3g1dXZvZnplOXA1bHpza2Y1NG1yeGtnMHllZzBjNnFoZyZlcD12MV9naWZzX3NlYXJjaCZjdD1n/IgOEWPOgK6uVa/200w.gif"
                        )
                    }
                )).collectAsLazyPagingItems(),
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedVisibilityScope = this,
            )
        }
    }
}