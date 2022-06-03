package com.example.mediainfocompose.data

import com.example.mediainfocompose.data.model.Image
import com.example.mediainfocompose.data.model.Video
import kotlinx.coroutines.flow.Flow

interface MediaDataSource {
    suspend fun queryAllImageList(): Flow<List<Image>>
    suspend fun queryAllVideoList(): Flow<List<Video>>
}