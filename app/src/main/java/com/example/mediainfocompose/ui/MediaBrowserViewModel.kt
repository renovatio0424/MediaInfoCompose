package com.example.mediainfocompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediainfocompose.data.AndroidMediaDataSource
import com.example.mediainfocompose.data.model.MediaInfo
import com.example.mediainfocompose.data.model.MediaType
import com.example.mediainfocompose.data.model.MediaType.Audio
import com.example.mediainfocompose.data.model.MediaType.ETC
import com.example.mediainfocompose.data.model.MediaType.Image
import com.example.mediainfocompose.data.model.MediaType.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaBrowserViewModel @Inject constructor(
    private val mediaDataSource: AndroidMediaDataSource
) : ViewModel() {
    private var _audioUiState: MutableStateFlow<MediaBrowserUiState> = MutableStateFlow(MediaBrowserUiState.Loading)
    val audioUiState: StateFlow<MediaBrowserUiState> = _audioUiState

    private var _videoUiState: MutableStateFlow<MediaBrowserUiState> = MutableStateFlow(MediaBrowserUiState.Loading)
    val videoUiState: StateFlow<MediaBrowserUiState> = _videoUiState

    private var _imageUiState: MutableStateFlow<MediaBrowserUiState> = MutableStateFlow(MediaBrowserUiState.Loading)
    val imageUiState: StateFlow<MediaBrowserUiState> = _imageUiState


    init {
        loadMediaInfoList(Image)
        loadMediaInfoList(Video)
        loadMediaInfoList(Audio)
    }

    private fun loadMediaInfoList(mediaType: MediaType) {
        val uiState = getUiState(mediaType) ?: return

        uiState.update { MediaBrowserUiState.Loading }
        viewModelScope.launch {
            mediaDataSource.queryMediaInfoList(mediaType)
                .catch { exception ->
                    uiState.update { MediaBrowserUiState.Failure(exception) }
                }
                .collect { mediaInfoList ->
                    uiState.update { MediaBrowserUiState.Success(mediaInfoList) }
                }
        }
    }

    private fun getUiState(mediaType: MediaType): MutableStateFlow<MediaBrowserUiState>? {
        return when (mediaType) {
            Video -> _videoUiState
            Image -> _imageUiState
            Audio -> _audioUiState
            ETC -> return null
        }
    }
}

sealed class MediaBrowserUiState {
    object Loading : MediaBrowserUiState()
    class Success(val mediaInfoList: List<MediaInfo>) : MediaBrowserUiState()
    class Failure(val exception: Throwable) : MediaBrowserUiState()
}

