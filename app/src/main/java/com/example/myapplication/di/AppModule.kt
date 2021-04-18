package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.CharacterDao
import com.example.myapplication.data.remote.QuotesService
import com.example.myapplication.data.remote.CharacterRemoteDataSource
import com.example.myapplication.data.remote.CharacterService
import com.example.myapplication.data.repository.CharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Images

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Quotes

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Images
    @Provides
    fun provideRetrofit0() : Retrofit = Retrofit.Builder()
        .baseUrl("https://thronesapi.com/api/v2/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Singleton
    @Quotes
    @Provides
    fun provideRetrofit1() : Retrofit = Retrofit.Builder()
        .baseUrl("https://game-of-thrones-quotes.herokuapp.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    fun provideCharacterService(@Images retrofit0: Retrofit): CharacterService = retrofit0.create(CharacterService::class.java)

    @Provides
    fun provideQuotesService(@Quotes retrofit1: Retrofit): QuotesService = retrofit1.create(QuotesService::class.java)

    @Singleton
    @Provides
    fun provideCharacterRemoteDataSource(characterService: CharacterService, quotesService: QuotesService) = CharacterRemoteDataSource(characterService, quotesService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideCharacterDao(db: AppDatabase) = db.characterDao()

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: CharacterRemoteDataSource, localDataSource: CharacterDao) =
        CharacterRepository(remoteDataSource, localDataSource)
}