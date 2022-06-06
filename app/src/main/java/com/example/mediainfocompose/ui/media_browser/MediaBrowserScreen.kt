package com.example.mediainfocompose.ui.media_browser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mediainfocompose.R.string
import com.example.mediainfocompose.data.model.Video
import com.example.mediainfocompose.ui.LoadingView
import com.example.mediainfocompose.ui.MediaBrowserUiState
import com.example.mediainfocompose.ui.MediaBrowserUiState.Failure
import com.example.mediainfocompose.ui.MediaBrowserUiState.Loading
import com.example.mediainfocompose.ui.MediaBrowserUiState.Success
import com.example.mediainfocompose.ui.theme.MediaInfoComposeTheme
import com.example.mediainfocompose.ui.toReadableByteString
import com.example.mediainfocompose.ui.widget.ErrorView
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MediaBrowserScreen(mediaBrowserUiState: MediaBrowserUiState) {
    when (mediaBrowserUiState) {
        is Loading -> LoadingView()
        is Success -> MediaListScreen(mediaBrowserUiState.videoList)
        is Failure -> ErrorView(mediaBrowserUiState.exception.localizedMessage)
    }
}

@Composable
fun MediaListScreen(videoList: List<Video>) {
    LazyVerticalGrid(
        columns = Fixed(2),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(videoList) { video ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(2.dp)
            ) {
                VideoThumbnailView(video)
                VideoInfoView(Modifier.align(Alignment.BottomEnd), video)
            }
        }
    }
}

@Composable
private fun VideoInfoView(modifier: Modifier, video: Video) {
    Row(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(0.1f),
                        Color.Black.copy(0.5f),
                        Color.Black.copy(1.0f),
                    )
                )
            )
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Text(text = video.size.toReadableByteString(), color = Color.White, textAlign = TextAlign.End)
    }
}

@Composable
private fun VideoThumbnailView(video: Video) {
    GlideImage(
        imageModel = video.uri,
        contentScale = ContentScale.Crop,
        circularReveal = CircularReveal(duration = 350),
        loading = {
            Box(modifier = Modifier.matchParentSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        failure = {
            Text(text = stringResource(string.message_image_load_fail))
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    MediaInfoComposeTheme {
        MediaBrowserScreen(
            mediaBrowserUiState = Success(
                listOf(
                    Video(1L, "video 1", 1000, 1000 * 1000),
                    Video(2L, "video 2", 2000, 2000 * 1000),
                    Video(3L, "video 3", 3000, 3000 * 1000)
                )
            )
        )
    }
}