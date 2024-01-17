package com.tzh.retrofit_module.di

import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.data.repository.BookInRepository
import com.tzh.retrofit_module.data.repository.UserRepository
import com.tzh.retrofit_module.data.repository_impl.BookInRepositoryImpl
import com.tzh.retrofit_module.data.repository_impl.UserRepositoryImpl
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
    fun providesLoginRepository(apiService: ApiService): UserRepository {
        return UserRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun providesBookInRepository(apiService: ApiService): BookInRepository {
        return BookInRepositoryImpl(apiService)
    }
}