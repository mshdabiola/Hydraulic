/*
 *abiola 2022
 */

package com.mshdabiola.main

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mshdabiola.data.model.Result
import com.mshdabiola.designsystem.component.SkLoadingWheel
import com.mshdabiola.designsystem.component.scrollbar.DraggableScrollbar
import com.mshdabiola.designsystem.component.scrollbar.rememberDraggableScroller
import com.mshdabiola.designsystem.component.scrollbar.scrollbarState
import com.mshdabiola.designsystem.theme.LocalTintTheme
import com.mshdabiola.model.Image
import com.mshdabiola.model.naviagation.Detail
import com.mshdabiola.ui.NoteUiState
import com.mshdabiola.ui.collectAsStateWithLifecycleCommon
import com.mshdabiola.ui.noteItems
import hydraulic.features.main.generated.resources.Res
import hydraulic.features.main.generated.resources.features_main_empty_description
import hydraulic.features.main.generated.resources.features_main_empty_error
import hydraulic.features.main.generated.resources.features_main_img_empty_bookmarks
import hydraulic.features.main.generated.resources.features_main_loading
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

// import org.koin.androidx.compose.koinViewModel

@OptIn(KoinExperimentalAPI::class, ExperimentalSharedTransitionApi::class)
@Composable
internal fun MainRoute(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    navigateToDetail: (Detail) -> Unit,
//    viewModel: MainViewModel,
) {
    val viewModel: MainViewModel = koinViewModel()

    val feedNote = viewModel.feedUiMainState.collectAsStateWithLifecycleCommon()

    MainScreen(
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        modifier = modifier,
        mainState = feedNote.value,
        navigateToDetail = navigateToDetail,
        //   items = timeline,

    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalSharedTransitionApi::class,
)
@Composable
internal fun MainScreen(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    mainState: Result<List<NoteUiState>>,
    navigateToDetail: (Detail) -> Unit = {},
) {
    val state = rememberLazyListState()
    with(sharedTransitionScope) {
        Box(
            modifier = modifier.sharedBounds(
                sharedContentState = rememberSharedContentState("container"),
                animatedVisibilityScope = animatedContentScope,
            ),
        ) {
            LazyColumn(
                state = state,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .testTag("main:list"),
            ) {
                item {
                    // Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                }
                when (mainState) {
                    is Result.Loading -> item {
                        LoadingState()
                    }

                    is Result.Error -> TODO()
                    is Result.Success -> {
                        if (mainState.data.isEmpty()) {
                            item {
                                EmptyState()
                            }
                        } else {
                            noteItems(
                                modifier = Modifier,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedContentScope = animatedContentScope,
                                items = mainState.data,
                                onNoteClick = { navigateToDetail(Detail(it)) },
                            )
                        }
                    }
                }
                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
            val itemsAvailable = noteUiStateItemsSize(mainState)
            val scrollbarState = state.scrollbarState(
                itemsAvailable = itemsAvailable,
            )
            state.DraggableScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .padding(horizontal = 2.dp)
                    .align(Alignment.CenterEnd),
                state = scrollbarState,
                orientation = Orientation.Vertical,
                onThumbMoved = state.rememberDraggableScroller(
                    itemsAvailable = itemsAvailable,
                ),
            )
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    SkLoadingWheel(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .testTag("main:loading"),
        contentDesc = stringResource(Res.string.features_main_loading),
    )
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .testTag("main:empty"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val iconTint = LocalTintTheme.current.iconTint
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(Res.drawable.features_main_img_empty_bookmarks),
            colorFilter = if (iconTint != Color.Unspecified) ColorFilter.tint(iconTint) else null,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(Res.string.features_main_empty_error),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.features_main_empty_description),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
private fun noteUiStateItemsSize(
    topicUiState: Result<List<NoteUiState>>,
) = when (topicUiState) {
    is Result.Error -> 0 // Nothing
    is Result.Loading -> 1 // Loading bar
    is Result.Success -> topicUiState.data.size + 2
}

@Composable
fun ItemImage(imageModel: Image) {
    ListItem(
        headlineContent = { Text(imageModel.user ?: "name") },
        leadingContent = {
//            AsyncImage(
//                modifier = Modifier.size(150.dp),
//                model = imageModel.url,
//                contentDescription = null,
//            )
        },
    )
}
