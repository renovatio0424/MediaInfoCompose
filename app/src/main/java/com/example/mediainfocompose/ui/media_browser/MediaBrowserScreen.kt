package com.example.mediainfocompose.ui.media_browser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Audiotrack
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.mediainfocompose.data.model.MediaInfo
import com.example.mediainfocompose.data.model.MediaType
import com.example.mediainfocompose.data.model.MediaType.Audio
import com.example.mediainfocompose.data.model.MediaType.ETC
import com.example.mediainfocompose.data.model.MediaType.Image
import com.example.mediainfocompose.data.model.MediaType.Video
import com.example.mediainfocompose.ui.theme.MediaInfoComposeTheme
import com.example.mediainfocompose.ui.toReadableByteString
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MediaBrowserScreen(mediaInfoList: List<MediaInfo>) {
    MediaListView(mediaInfoList)
}

@Composable
fun MediaListView(mediaInfoList: List<MediaInfo>) {
    LazyVerticalGrid(
        columns = Fixed(2),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(mediaInfoList) { mediaInfo ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(2.dp)
            ) {
                VideoThumbnailView(mediaInfo)
                VideoInfoView(Modifier.align(Alignment.BottomEnd), mediaInfo)
            }
        }
    }
}

fun getMediaInfoIcon(mediaType: MediaType): ImageVector {
    return when (mediaType) {
        Video -> Icons.Outlined.Movie
        Image -> Icons.Outlined.Image
        Audio -> Icons.Outlined.Audiotrack
        ETC -> Icons.Outlined.Description
    }
}

@Composable
private fun VideoInfoView(modifier: Modifier, mediaInfo: MediaInfo) {
    Row(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(0f),
                        Color.Black.copy(0.1f),
                        Color.Black.copy(1.0f),
                    )
                )
            )
            .fillMaxSize()
    ) {
        Text(
            text = mediaInfo.size.toReadableByteString(),
            color = Color.White,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Bottom)
        )
    }
}

@Composable
private fun VideoThumbnailView(mediaInfo: MediaInfo) {
    GlideImage(
        imageModel = mediaInfo.uri,
        requestOptions = {
            RequestOptions()
                .override(300)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        },
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
            Box(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .align(Alignment.Center)
                    .background(Color.LightGray)
            ) {
                Icon(
                    imageVector = getMediaInfoIcon(mediaInfo.mediaType), contentDescription = null, modifier =
                    Modifier.align(Alignment.Center)
                )
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    MediaInfoComposeTheme {
        MediaBrowserScreen(
            listOf(
                MediaInfo(1L, "video 1", 1000, 1000 * 1000, MediaType.Video),
                MediaInfo(2L, "video 2", 2000, 2000 * 1000, MediaType.Video),
                MediaInfo(3L, "video 3", 3000, 3000 * 1000, MediaType.Video)
            )
        )
    }
}