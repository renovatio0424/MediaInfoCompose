package com.example.mediainfocompose.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mediainfocompose.data.model.Video
import com.example.mediainfocompose.ui.MediaBrowserUiState.Success
import com.example.mediainfocompose.ui.media_browser.MediaBrowserScreen
import com.example.mediainfocompose.ui.theme.MediaInfoComposeTheme
import com.example.mediainfocompose.ui.widget.RequestPermissionScreen
import com.example.mediainfocompose.ui.widget.SkeletonItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mediaBrowserViewModel: MediaBrowserViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MediaInfoComposeTheme {
                val storagePermissionState =
                    rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)

                when (storagePermissionState.status) {
                    is PermissionStatus.Granted -> MainScreen(mediaBrowserViewModel)
                    is PermissionStatus.Denied -> RequestPermissionScreen(
                        storagePermissionState.status.shouldShowRationale,
                        storagePermissionState::launchPermissionRequest
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(mediaBrowserViewModel: MediaBrowserViewModel) {
    // A surface container using the 'background' color from the theme
    val uiState by mediaBrowserViewModel.uiState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        MediaBrowserScreen(uiState)
    }
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

@Composable
fun LoadingView() {
    Column(
        Modifier.padding(8.dp),
        Arrangement.spacedBy(8.dp)
    ) {
        repeat(10) {
            SkeletonItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoadingPreview() {
    MediaInfoComposeTheme {
        LoadingView()
    }
}
