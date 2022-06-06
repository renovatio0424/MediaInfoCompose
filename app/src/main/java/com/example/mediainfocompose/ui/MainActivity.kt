package com.example.mediainfocompose.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mediainfocompose.data.model.MediaInfo
import com.example.mediainfocompose.data.model.MediaType
import com.example.mediainfocompose.data.model.MediaType.Audio
import com.example.mediainfocompose.data.model.MediaType.Image
import com.example.mediainfocompose.data.model.MediaType.Video
import com.example.mediainfocompose.ui.MediaBrowserUiState.Failure
import com.example.mediainfocompose.ui.MediaBrowserUiState.Loading
import com.example.mediainfocompose.ui.MediaBrowserUiState.Success
import com.example.mediainfocompose.ui.media_browser.MediaBrowserScreen
import com.example.mediainfocompose.ui.media_browser.MediaListView
import com.example.mediainfocompose.ui.theme.MediaInfoComposeTheme
import com.example.mediainfocompose.ui.widget.ErrorView
import com.example.mediainfocompose.ui.widget.MediaInfoTabRow
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mediaBrowserViewModel: MediaBrowserViewModel) {
    // A surface container using the 'background' color from the theme
    val videoUiState by mediaBrowserViewModel.videoUiState.collectAsState()
    val imageUiState by mediaBrowserViewModel.imageUiState.collectAsState()
    val audioUiState by mediaBrowserViewModel.audioUiState.collectAsState()

    val allScreens = MediaInfoTab.values().toList()
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = MediaInfoTab.fromRoute(backStackEntry.value?.destination?.route)

    Scaffold(
        topBar = {
            MediaInfoTabRow(
                allScreens = allScreens,
                onTabSelected = { screen -> navController.navigate(screen.name) },
                currentScreen = currentScreen
            )
        }
    ) { innerPadding ->
        MediaInfoNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            videoUiState = videoUiState,
            imageUiState = imageUiState,
            audioUiState = audioUiState
        )
    }
}

@Composable
fun MediaInfoNavHost(
    navController: NavHostController,
    modifier: Modifier,
    videoUiState: MediaBrowserUiState,
    imageUiState: MediaBrowserUiState,
    audioUiState: MediaBrowserUiState
) {
    NavHost(
        navController = navController,
        startDestination = MediaInfoTab.Video.name,
        modifier = modifier
    ) {
        composable(MediaInfoTab.Image.name) {
            UiStateView(imageUiState, Image)
        }
        composable(MediaInfoTab.Video.name) {
            UiStateView(videoUiState, Video)
        }
        composable(MediaInfoTab.Audio.name) {
            UiStateView(audioUiState, Audio)
        }
    }
}

@Composable
private fun UiStateView(uiState: MediaBrowserUiState, mediaType: MediaType) {
    when (uiState) {
        is Loading -> LoadingView()
        is Success -> MediaListView(uiState.mediaInfoList.filter { it.mediaType == mediaType })
        is Failure -> ErrorView(uiState.exception.localizedMessage)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    MediaInfoComposeTheme {
        MediaBrowserScreen(
            listOf(
                MediaInfo(1L, "video 1", 1000, 1000 * 1000, Video),
                MediaInfo(2L, "video 2", 2000, 2000 * 1000, Video),
                MediaInfo(3L, "video 3", 3000, 3000 * 1000, Video)
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
                    .fillMaxWidth(0.5f)
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
