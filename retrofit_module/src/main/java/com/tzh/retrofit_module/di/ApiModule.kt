package com.tzh.retrofit_module.di

import android.content.Context
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.data.network.NetworkClientBuilder
import com.tzh.retrofit_module.data.repository.TokenRepository
import com.tzh.retrofit_module.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun providesNetworkClientBuilder(
        @ApplicationContext context: Context,
        tokenRepository: TokenRepository
    ): NetworkClientBuilder {
        return NetworkClientBuilder(context,tokenRepository)
    }

    @Provides
    @Singleton
    fun providesApiService(networkClientBuilder: NetworkClientBuilder): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(networkClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}