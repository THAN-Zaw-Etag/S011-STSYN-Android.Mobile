package com.tzh.retrofit_module.di

import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.data.repository.AccountCheckRepositoryImpl
import com.tzh.retrofit_module.data.repository.BookInRepositoryImpl
import com.tzh.retrofit_module.data.repository.BookOutRepositoryImpl
import com.tzh.retrofit_module.data.repository.CheckInOutRepositoryImpl
import com.tzh.retrofit_module.data.repository.TokenRepository
import com.tzh.retrofit_module.data.repository.UserRepositoryImpl
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.repository.AccountCheckRepository
import com.tzh.retrofit_module.domain.repository.BookInRepository
import com.tzh.retrofit_module.domain.repository.BookOutRepository
import com.tzh.retrofit_module.domain.repository.checkIn.CheckInOutRepository
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
        localDataStore: LocalDataStore,
        appConfiguration: AppConfiguration,
    ): BookInRepository {
        return BookInRepositoryImpl(apiService, localDataStore, appConfiguration)
    }

    @Provides
    @Singleton
    fun providesBookOutRepository(
        apiService: ApiService,
        localDataStore: LocalDataStore,
        appConfiguration: AppConfiguration
    ): BookOutRepository {
        return BookOutRepositoryImpl(apiService, localDataStore, appConfiguration)
    }

    @Provides
    @Singleton
    fun providesCheckInOutRepository(
        apiService: ApiService,
        appConfiguration: AppConfiguration
    ): CheckInOutRepository {
        return CheckInOutRepositoryImpl(apiService,appConfiguration)
    }

    @Provides
    @Singleton
    fun provideTokenRepository(localDataStore: LocalDataStore): TokenRepository {
        return TokenRepository(localDataStore)
    }

    @Provides
    @Singleton
    fun providesAccountabilityCheckRepository(apiService: ApiService, localDataStore: LocalDataStore): AccountCheckRepository{
        return AccountCheckRepositoryImpl(apiService,localDataStore)
    }
}