package com.example.mediainfocompose.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.mediainfocompose.data.model.Video
import com.example.mediainfocompose.ui.MediaBrowserUiState.Failure
import com.example.mediainfocompose.ui.MediaBrowserUiState.Loading
import com.example.mediainfocompose.ui.MediaBrowserUiState.Success
import com.example.mediainfocompose.ui.theme.MediaInfoComposeTheme
import com.gun0912.tedpermission.coroutine.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MediaBrowserActivity : ComponentActivity() {
    private val mediaBrowserViewModel: MediaBrowserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            TedPermission.create().setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).check()
        }

        setContent {
            MediaInfoComposeTheme {
                // A surface container using the 'background' color from the theme
                val uiState by mediaBrowserViewModel.uiState.collectAsState()

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MediaBrowserScreen(uiState)
                }
            }
        }
    }
}

@Composable
fun MediaBrowserScreen(mediaBrowserUiState: MediaBrowserUiState) {
    when (mediaBrowserUiState) {
        is Loading -> {
            Log.d("reno", "Loading")
        }
        is Success -> MediaListScreen(mediaBrowserUiState.videoList)
        is Failure -> {
            Log.e("reno", "Failure: ${mediaBrowserUiState.exception.printStackTrace()}")
        }
    }
}

@Composable
fun MediaListScreen(videoList: List<Video>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(videoList) { video ->
            Row {
                Text(text = video.id.toString())
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = video.name)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = video.duration.toString())
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = video.size.toString())
            }
        }
    }
}

@Preview(showBackground = true)
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