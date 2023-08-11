package com.vn.talktoai.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vn.talktoai.BuildConfig
import com.vn.talktoai.data.database.AppDatabase
import com.vn.talktoai.data.database.dao.ChatDao
import com.vn.talktoai.data.database.dao.MessageDao
import com.vn.talktoai.infrastructure.Constants.SERVER_TIMEOUT
import com.vn.talktoai.data.network.ApiService
import com.vn.talktoai.data.network.HeaderInterceptor
import com.vn.talktoai.data.repositoryimpl.ChatRepositoryImpl
import com.vn.talktoai.data.repositoryimpl.MessageRepositoryImpl
import com.vn.talktoai.domain.repositories.ChatRepository
import com.vn.talktoai.domain.repositories.MessageRepository
import com.vn.talktoai.domain.usecases.ChatUseCase
import com.vn.talktoai.domain.usecases.MainUseCase
import com.vn.talktoai.presentation.chat.ChatUseCaseImpl
import com.vn.talktoai.presentation.main.MainUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideHeaderInterceptor(): HeaderInterceptor {
        return HeaderInterceptor()
    }

    @Singleton
    @Provides
    fun provideHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(SERVER_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(SERVER_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(SERVER_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)
            .addInterceptor(HttpLoggingInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }

    @Singleton
    @Provides
    fun provideChatDao(db: AppDatabase) = db.chatDao()

    @Singleton
    @Provides
    fun provideMessageDao(db: AppDatabase) = db.messageDao()

    @Singleton
    @Provides
    fun provideChatRepository(
        chatDao: ChatDao
    ): ChatRepository {
        return ChatRepositoryImpl(chatDao)
    }

    @Singleton
    @Provides
    fun provideMessageRepository(
        messageDao: MessageDao,
        apiService: ApiService
    ): MessageRepository {
        return MessageRepositoryImpl(messageDao, apiService)
    }

    @Singleton
    @Provides
    fun provideChatUseCase(messageRepository: MessageRepository): ChatUseCase {
        return ChatUseCaseImpl(messageRepository)
    }

    @Singleton
    @Provides
    fun provideMainUseCase(chatRepository: ChatRepository): MainUseCase {
        return MainUseCaseImpl(chatRepository)
    }
}