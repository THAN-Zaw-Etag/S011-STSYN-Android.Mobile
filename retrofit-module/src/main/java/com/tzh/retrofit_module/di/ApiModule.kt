package com.tzh.retrofit_module.di

import android.content.Context
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.data.network.BaseUrlProvider
import com.tzh.retrofit_module.data.network.NetworkClientBuilder
import com.tzh.retrofit_module.data.repository.TokenRepository
import com.tzh.retrofit_module.data.settings.AppConfiguration
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
    fun provideBaseUrlProvider(
        appConfiguration: AppConfiguration
    ): BaseUrlProvider {
        return BaseUrlProvider(appConfiguration)
    }

    @Provides
    @Singleton
    fun providesNetworkClientBuilder(
        @ApplicationContext context: Context,
        tokenRepository: TokenRepository,
        baseUrlProvider: BaseUrlProvider
        ): NetworkClientBuilder {
        return NetworkClientBuilder(context,tokenRepository,baseUrlProvider)
    }
    @Provides
    @Singleton
    fun providesApiService(
        networkClientBuilder: NetworkClientBuilder,
        baseUrlProvider: BaseUrlProvider
    ): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrlProvider.getBaseUrl())
            .client(networkClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}