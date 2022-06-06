package com.example.mediainfocompose.fake_data

import com.example.mediainfocompose.data.model.MediaInfo
import com.example.mediainfocompose.data.model.MediaType.Image
import com.example.mediainfocompose.data.model.MediaType.Video

object FakeMediaData {
    val fakeAllVideoList = listOf(
        MediaInfo(1L, "video 1", 1000, 1000 * 1000, Video),
        MediaInfo(2L, "video 2", 2000, 2000 * 1000, Video),
        MediaInfo(3L, "video 3", 3000, 3000 * 1000, Video)
    )

    val fakeAllImageList = listOf(
        MediaInfo(1, "image 1", 1000, 1000 * 1000, Image),
        MediaInfo(2, "image 2", 2000, 2000 * 1000, Image),
        MediaInfo(3, "image 3", 3000, 3000 * 1000, Image)
    )
}