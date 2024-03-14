package com.etag.stsyn.di

import android.app.Application
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.data.settings.AppConfiguration
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
    fun providesRfidHandler(application: Application, appConfiguration: AppConfiguration): ZebraRfidHandler =
        ZebraRfidHandler(application, appConfiguration)
}
