package com.jasmeet.chaintechassignment.di

import android.content.Context
import androidx.room.Room
import com.jasmeet.chaintechassignment.dao.AuthDao
import com.jasmeet.chaintechassignment.dataBase.AuthDatabase
import com.jasmeet.chaintechassignment.repository.AuthDataRepository
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
    fun provideAuthDatabase(@ApplicationContext context: Context): AuthDatabase {
        return Room.databaseBuilder(
            context,
            AuthDatabase::class.java,
            "auth_database"
        ).build()
    }

    @Provides
    fun provideAuthDao(database: AuthDatabase): AuthDao {
        return database.authDao()
    }

    @Provides
    fun provideAuthDataRepository(dao: AuthDao):AuthDataRepository{
        return AuthDataRepository(dao)
    }
}
