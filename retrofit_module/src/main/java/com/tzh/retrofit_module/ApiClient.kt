package com.tzh.retrofit_module

import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import okhttp3.OkHttpClient as OkHttpClient1

object ApiClient {

    private val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })

    private fun createLoggingInterceptor() = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private fun createOKHttpClient(): OkHttpClient1 {
        return OkHttpClient1.Builder().apply {
            sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            hostnameVerifier { _, _ -> true }.addInterceptor(createLoggingInterceptor())
            readTimeout(30, TimeUnit.MINUTES)
            writeTimeout(30, TimeUnit.MINUTES)
            connectTimeout(30, TimeUnit.MINUTES)
            protocols(listOf(Protocol.HTTP_1_1))
        }.build()
    }

    private fun createClientForHealthCheck(): OkHttpClient1 {
        return OkHttpClient1.Builder().apply {
            sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            hostnameVerifier { _, _ -> true }.addInterceptor(createLoggingInterceptor())
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            connectTimeout(30, TimeUnit.SECONDS)
            protocols(listOf(Protocol.HTTP_1_1))
        }.build()
    }


    //    val sslContext = SSLContext.getInstance("SSL")
    private val sslContext = SSLContext.getInstance("TLS")

    private fun getClient(url: String): Retrofit {
        sslContext.init(null, trustAllCerts, SecureRandom())
        return Retrofit.Builder().apply {
            baseUrl(url)
            addConverterFactory(GsonConverterFactory.create())
            client(createOKHttpClient())
        }.build()
    }

    fun createApiInterface(url: String): BaseApiInterface = getClient(url).create(BaseApiInterface::class.java)

    fun createApiInterForHealthCheck(url: String): BaseApiInterface =
        Retrofit.Builder().apply {
            sslContext.init(null, trustAllCerts, SecureRandom())
            baseUrl(url)
            addConverterFactory(GsonConverterFactory.create())
            client(createClientForHealthCheck())
        }.build().create(BaseApiInterface::class.java)


}