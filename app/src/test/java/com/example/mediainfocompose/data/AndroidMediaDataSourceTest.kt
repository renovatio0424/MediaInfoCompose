package com.example.mediainfocompose.data

import android.content.ContentResolver
import android.database.MatrixCursor
import android.provider.MediaStore
import com.example.mediainfocompose.fake_data.FakeMediaData
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner

//reference : https://github.com/Kpeved/Content-Resolver-test/blob/master/app/src/test/java/com/lolkek/contactlist/ContactsHelperTest.kt
@RunWith(RobolectricTestRunner::class)
class AndroidMediaDataSourceTest {
    private lateinit var allVideoCursor: MatrixCursor
    private lateinit var allImageCursor: MatrixCursor

    private val fakeAllVideoList = FakeMediaData.fakeAllVideoList
    private val fakeAllImageList = FakeMediaData.fakeAllImageList

    @Before
    fun setUp() {
        allVideoCursor = MatrixCursor(
            arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE
            )
        )

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

        allImageCursor = MatrixCursor(
            arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DURATION,
                MediaStore.Images.Media.SIZE
            )
        )

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

        val result = androidMediaDataSource.queryAllImageList().first()

        assertEquals(expect, result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun queryAllVideoList() = runTest {
        val expect = fakeAllVideoList
        val androidMediaDataSource = createTestAndroidMediaDataSource(allVideoCursor, testScheduler)

        val result = androidMediaDataSource.queryAllVideoList().first()

        assertEquals(expect, result)
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
}