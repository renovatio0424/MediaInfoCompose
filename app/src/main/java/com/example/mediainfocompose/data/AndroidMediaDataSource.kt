package com.example.mediainfocompose.data

import android.content.ContentResolver
import com.example.mediainfocompose.data.model.MediaInfo
import com.example.mediainfocompose.data.model.MediaType
import com.example.mediainfocompose.data.model.MediaType.Audio
import com.example.mediainfocompose.data.model.MediaType.Image
import com.example.mediainfocompose.data.model.MediaType.Video
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AndroidMediaDataSource @Inject constructor(
    private val contentResolver: ContentResolver,
    override val coroutineContext: CoroutineContext = Dispatchers.IO
) : MediaDataSource, CoroutineScope {

    override suspend fun queryMediaInfoList(mediaType: MediaType): Flow<List<MediaInfo>> =
        withContext(coroutineContext) {
            val query = contentResolver.query(
                mediaType.collection,
                mediaType.projection,
                null,
                null,
                null
            )

            val mediaInfoList = mutableListOf<MediaInfo>()

            query?.use { cursor ->
                // Cache column indices.
                val idColumn = cursor.getColumnIndexOrThrow(mediaType.idColumnIdx)
                val nameColumn = cursor.getColumnIndexOrThrow(mediaType.displayColumnIdx)
                val durationColumn = cursor.getColumnIndexOrThrow(mediaType.durationColumnIdx)
                val sizeColumn = cursor.getColumnIndexOrThrow(mediaType.sizeColumnIdx)

                while (cursor.moveToNext()) {
                    // Get values of columns for a given video.
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val duration = cursor.getInt(durationColumn)
                    val size = cursor.getInt(sizeColumn)

                    // Stores column values and the contentUri in a local object
                    // that represents the media file.
                    mediaInfoList += MediaInfo(id, name, duration, size, mediaType)
                }
            }

            return@withContext flow { emit(mediaInfoList) }
        }

    @OptIn(FlowPreview::class)
    override suspend fun queryAllMediaInfoList(): Flow<List<MediaInfo>> = withContext(coroutineContext) {
        return@withContext flowOf(queryMediaInfoList(Image), queryMediaInfoList(Video), queryMediaInfoList(Audio))
            .flattenMerge()
    }
}