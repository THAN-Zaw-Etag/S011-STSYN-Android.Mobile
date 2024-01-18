package com.etag.stsyn.di

import android.content.Context
import com.tzh.retrofit_module.data.localStorage.LocalDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesDataStore(@ApplicationContext context: Context): LocalDataStore {
        return LocalDataStore(context)
    }
}
