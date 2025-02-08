package com.ahrokholska.gifs.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.ahrokholska.gifs.data.Constants
import com.ahrokholska.gifs.data.local.AppDatabase
import com.ahrokholska.gifs.data.local.AppDatabase.Companion.MIGRATION_1_2
import com.ahrokholska.gifs.data.local.AppDatabase.Companion.MIGRATION_2_3
import com.ahrokholska.gifs.data.network.GifService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideRoomDb(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, AppDatabase::class.java, "app_database"
    )
        .addMigrations(MIGRATION_1_2)
        .addMigrations(MIGRATION_2_3)
        .build()

    @Provides
    @Singleton
    fun provideGifDao(database: AppDatabase) = database.gifDao()

    @Provides
    @Singleton
    fun provideRemoteKeyDao(database: AppDatabase) = database.remoteKeyDao()

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

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)
}