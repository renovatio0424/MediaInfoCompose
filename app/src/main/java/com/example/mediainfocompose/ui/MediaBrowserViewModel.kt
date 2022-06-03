package com.example.mediainfocompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediainfocompose.data.AndroidMediaDataSource
import com.example.mediainfocompose.data.model.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaBrowserViewModel @Inject constructor(
    private val mediaDataSource: AndroidMediaDataSource
) : ViewModel() {
    private var _uiState: MutableStateFlow<MediaBrowserUiState> = MutableStateFlow(MediaBrowserUiState.Loading)
    val uiState: StateFlow<MediaBrowserUiState> = _uiState

    init {
        loadMediaInfoList()
    }

    private fun loadMediaInfoList() {
        _uiState.update { MediaBrowserUiState.Loading }
        viewModelScope.launch {
            mediaDataSource.queryAllVideoList()
                .catch { exception ->
                    _uiState.update { MediaBrowserUiState.Failure(exception) }
                }
                .collect { videoList ->
                    _uiState.update { MediaBrowserUiState.Success(videoList) }
                }
        }
    }
}

sealed class MediaBrowserUiState {
    object Loading : MediaBrowserUiState()
    class Success(val videoList: List<Video>) : MediaBrowserUiState()
    class Failure(val exception: Throwable) : MediaBrowserUiState()
}

