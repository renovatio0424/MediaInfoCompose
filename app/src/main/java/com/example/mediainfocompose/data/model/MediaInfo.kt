package com.example.mediainfocompose.data.model

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import com.example.mediainfocompose.data.model.MediaType.Audio
import com.example.mediainfocompose.data.model.MediaType.ETC
import com.example.mediainfocompose.data.model.MediaType.Image
import com.example.mediainfocompose.data.model.MediaType.Video

data class MediaInfo(
    val id: Long,
    val name: String,
    val duration: Int,
    val size: Int,
    val mediaType: MediaType
) {
    val uri: Uri?
        get() {
            val contentUri = when (mediaType) {
                Image -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                Video -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                Audio -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                ETC -> return null
            }

            return ContentUris.withAppendedId(contentUri, id)
        }
}