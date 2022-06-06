package com.example.mediainfocompose.data.model

import android.net.Uri
import android.os.Build
import android.provider.MediaStore

enum class MediaType(
    val collection: Uri,
    val projection: Array<String>,
) {
    Video(videoCollection, videoProjection),
    Image(imageCollection, imageProjection),
    Audio(audioCollection, audioProjection),
    ETC(Uri.EMPTY, arrayOf());

    val idColumnIdx
        get() = projection[0]
    val displayColumnIdx
        get() = projection[1]
    val durationColumnIdx
        get() = projection[2]
    val sizeColumnIdx
        get() = projection[3]
}

private val videoCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    MediaStore.Video.Media.getContentUri(
        MediaStore.VOLUME_EXTERNAL
    )
} else {
    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
}

private val videoProjection = arrayOf(
    MediaStore.Video.Media._ID,
    MediaStore.Video.Media.DISPLAY_NAME,
    MediaStore.Video.Media.DURATION,
    MediaStore.Video.Media.SIZE
)

private val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    MediaStore.Images.Media.getContentUri(
        MediaStore.VOLUME_EXTERNAL
    )
} else {
    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
}

private val imageProjection = arrayOf(
    MediaStore.Images.Media._ID,
    MediaStore.Images.Media.DISPLAY_NAME,
    MediaStore.Images.Media.DURATION,
    MediaStore.Images.Media.SIZE
)

private val audioCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    MediaStore.Audio.Media.getContentUri(
        MediaStore.VOLUME_EXTERNAL
    )
} else {
    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
}

private val audioProjection = arrayOf(
    MediaStore.Audio.Media._ID,
    MediaStore.Audio.Media.DISPLAY_NAME,
    MediaStore.Audio.Media.DURATION,
    MediaStore.Audio.Media.SIZE
)