package com.example.mediainfocompose.data

import android.content.ContentResolver
import android.provider.MediaStore
import com.example.mediainfocompose.data.model.Image
import com.example.mediainfocompose.data.model.MediaType
import com.example.mediainfocompose.data.model.Video
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AndroidMediaDataSource @Inject constructor(
    private val contentResolver: ContentResolver,
    override val coroutineContext: CoroutineContext = Dispatchers.IO
) : MediaDataSource, CoroutineScope {

    override suspend fun queryAllImageList(): Flow<List<Image>> = withContext(coroutineContext) {
        val query = contentResolver.query(
            MediaType.Image.collection,
            MediaType.Image.projection,
            null,
            null,
            null
        )

        val imageList = mutableListOf<Image>()

        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                imageList += Image(id, name, duration, size)
            }
        }

        return@withContext flow { emit(imageList) }
    }

    override suspend fun queryAllVideoList(): Flow<List<Video>> = withContext(coroutineContext) {
        val query = contentResolver.query(
            MediaType.Video.collection,
            MediaType.Video.projection,
            null,
            null,
            null
        )
        val videoList = mutableListOf<Video>()

        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                videoList += Video(id, name, duration, size)
            }
        }

        return@withContext flow { emit(videoList) }
    }
}