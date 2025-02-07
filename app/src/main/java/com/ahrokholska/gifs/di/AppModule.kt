package com.ahrokholska.gifs.di

import com.ahrokholska.gifs.data.Constants
import com.ahrokholska.gifs.data.network.GifService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val url = chain
                        .request()
                        .url
                        .newBuilder()
                        .addQueryParameter("api_key", Constants.API_KEY)
                        .build()
                    chain.proceed(chain.request().newBuilder().url(url).build())
                }.build()

        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideGifService(retrofit: Retrofit): GifService = retrofit.create(GifService::class.java)
}