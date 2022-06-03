package com.example.mediainfocompose.data.model

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

data class Image(
    val id: Long,
    val name: String,
    val duration: Int,
    val size: Int
) {
    val uri: Uri
        get() = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
}