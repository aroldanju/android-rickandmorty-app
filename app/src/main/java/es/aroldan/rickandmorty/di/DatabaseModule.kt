package es.aroldan.rickandmorty.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.aroldan.rickandmorty.data.service.DatabaseService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabaseService(@ApplicationContext context: Context): DatabaseService =
        Room.databaseBuilder(context, DatabaseService::class.java, "rickandmorty-db")
            .build()
}