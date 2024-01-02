package com.etag.stsyn.di

import android.app.Application
import com.etag.stsyn.core.reader.ZebraRfidHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ReaderModule {
    @Provides
    @Singleton
    fun providesRfidHandler(application: Application): ZebraRfidHandler =
        ZebraRfidHandler(application)
}
