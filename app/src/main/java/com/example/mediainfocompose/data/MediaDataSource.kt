package com.example.mediainfocompose.data

import com.example.mediainfocompose.data.model.MediaInfo
import com.example.mediainfocompose.data.model.MediaType
import kotlinx.coroutines.flow.Flow

interface MediaDataSource {
    suspend fun queryMediaInfoList(mediaType: MediaType): Flow<List<MediaInfo>>
    suspend fun queryAllMediaInfoList(): Flow<List<MediaInfo>>
}