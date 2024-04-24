package com.mean.traclock.di

import android.content.Context
import androidx.room.Room
import com.mean.traclock.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context,
    ): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "database.db",
        ).build()

    @Provides
    fun provideRecordDao(database: AppDatabase) = database.recordDao()

    @Provides
    fun provideProjectDao(database: AppDatabase) = database.projectDao()
}
