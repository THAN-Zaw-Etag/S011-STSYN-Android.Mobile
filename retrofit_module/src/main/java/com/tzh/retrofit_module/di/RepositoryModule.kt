package com.tzh.retrofit_module.di

import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.data.repository_impl.BookInRepositoryImpl
import com.tzh.retrofit_module.data.repository_impl.UserRepositoryImpl
import com.tzh.retrofit_module.domain.repository.BookInRepository
import com.tzh.retrofit_module.domain.repository.UserRepository
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
    fun providesLoginRepository(
        apiService: ApiService,
        localDataStore: LocalDataStore
    ): UserRepository {
        return UserRepositoryImpl(apiService, localDataStore)
    }

    @Provides
    @Singleton
    fun providesBookInRepository(
        apiService: ApiService,
        localDataStore: LocalDataStore
    ): BookInRepository {
        return BookInRepositoryImpl(apiService, localDataStore)
    }
}