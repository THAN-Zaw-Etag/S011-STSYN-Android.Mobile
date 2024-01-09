package com.etag.stsyn.di

import com.etag.stsyn.data.repository.LoginRepository
import com.etag.stsyn.data.repository_impl.LoginRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providesLoginRepository(): LoginRepository {
        return LoginRepositoryImpl()
    }
}