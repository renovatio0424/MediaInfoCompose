package com.example.mediainfocompose.fake_data

import com.example.mediainfocompose.data.model.Image
import com.example.mediainfocompose.data.model.Video

object FakeMediaData {
    val fakeAllVideoList = listOf(
        Video(1L, "video 1", 1000, 1000 * 1000),
        Video(2L, "video 2", 2000, 2000 * 1000),
        Video(3L, "video 3", 3000, 3000 * 1000)
    )

    val fakeAllImageList = listOf(
        Image(1, "image 1", 1000, 1000 * 1000),
        Image(2, "image 2", 2000, 2000 * 1000),
        Image(3, "image 3", 3000, 3000 * 1000)
    )
}