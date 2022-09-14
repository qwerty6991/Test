package com.example.test.di

import android.content.Context
import androidx.room.Room
import com.example.test.data.room.AppDatabase
import com.example.test.data.room.GifsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DB_NAME
        )
            .build()
    }

    @Provides
    fun provideLaunchesDao(database: AppDatabase): GifsDao {
        return database.getLaunchesDao()
    }

    private companion object {
        const val DB_NAME = "launches.db"
    }
}