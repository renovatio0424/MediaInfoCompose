package com.example.mediainfocompose.data

import android.content.ContentResolver
import android.database.MatrixCursor
import com.example.mediainfocompose.data.model.MediaInfo
import com.example.mediainfocompose.data.model.MediaType.Image
import com.example.mediainfocompose.data.model.MediaType.Video
import com.example.mediainfocompose.fake_data.FakeMediaData
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.CountDownLatch

//reference : https://github.com/Kpeved/Content-Resolver-test/blob/master/app/src/test/java/com/lolkek/contactlist/ContactsHelperTest.kt
@RunWith(RobolectricTestRunner::class)
class AndroidMediaDataSourceTest {
    private lateinit var allVideoCursor: MatrixCursor
    private lateinit var allImageCursor: MatrixCursor

    private val fakeAllVideoList = FakeMediaData.fakeAllVideoList
    private val fakeAllImageList = FakeMediaData.fakeAllImageList

    @Before
    fun setUp() {
        allVideoCursor = MatrixCursor(Video.projection)

        fakeAllVideoList.forEach {
            allVideoCursor.addRow(
                arrayOf(
                    it.id,
                    it.name,
                    it.duration,
                    it.size
                )
            )
        }

        allImageCursor = MatrixCursor(Image.projection)

        fakeAllImageList.forEach {
            allImageCursor.addRow(
                arrayOf(
                    it.id,
                    it.name,
                    it.duration,
                    it.size
                )
            )
        }
    }

    @After
    fun close() {
        allVideoCursor.close()
        allImageCursor.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun queryAllImageList() = runTest {
        val expect = fakeAllImageList
        val androidMediaDataSource = createTestAndroidMediaDataSource(allImageCursor, testScheduler)

        val result = androidMediaDataSource.queryMediaInfoList(Image).first()

        assertEquals(expect, result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun queryAllVideoList() = runTest {
        val expect = fakeAllVideoList
        val androidMediaDataSource = createTestAndroidMediaDataSource(allVideoCursor, testScheduler)

        val result = androidMediaDataSource.queryMediaInfoList(Video).first()

        assertEquals(expect, result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun queryAllMediaInfoList() = runTest {
        val expect = fakeAllImageList + fakeAllVideoList
        val androidMediaDataSource = createTestAndroidMediaDataSource(allVideoCursor, allImageCursor, testScheduler)

        val countDownLatch = CountDownLatch(expect.size)
        val result = arrayListOf<MediaInfo>()

        androidMediaDataSource.queryAllMediaInfoList().collectLatest {
            it.forEach { mediaInfo ->
                result.add(mediaInfo)
                countDownLatch.countDown()
            }
        }

        countDownLatch.await()
        println("expect: $expect")
        println("result: $result")
        assertTrue(result.containsAll(expect))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun createTestAndroidMediaDataSource(
        mockCursor: MatrixCursor,
        testCoroutineScheduler: TestCoroutineScheduler
    ): AndroidMediaDataSource {
        val contentResolver: ContentResolver = mock {
            on {
                query(any(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())
            } doReturn mockCursor
        }
        val standardDispatcher = StandardTestDispatcher(testCoroutineScheduler)
        return AndroidMediaDataSource(contentResolver, standardDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun createTestAndroidMediaDataSource(
        videoMockCursor: MatrixCursor,
        imageMockCursor: MatrixCursor,
        testCoroutineScheduler: TestCoroutineScheduler
    ): AndroidMediaDataSource {
        val contentResolver: ContentResolver = mock {
            on {
                query(eq(Video.collection), eq(Video.projection), anyOrNull(), anyOrNull(), anyOrNull())
            } doReturn videoMockCursor
            on {
                query(eq(Image.collection), eq(Image.projection), anyOrNull(), anyOrNull(), anyOrNull())
            } doReturn imageMockCursor
        }
        val standardDispatcher = StandardTestDispatcher(testCoroutineScheduler)
        return AndroidMediaDataSource(contentResolver, standardDispatcher)
    }
}