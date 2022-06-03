package com.example.mediainfocompose.di

import android.content.ContentResolver
import android.content.Context
import com.example.mediainfocompose.data.AndroidMediaDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver = context.contentResolver

    @Provides
    fun provideMediaDataSource(contentResolver: ContentResolver) = AndroidMediaDataSource(contentResolver)
}