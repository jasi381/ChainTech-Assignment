package com.jasmeet.chaintechassignment.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jasmeet.chaintechassignment.dao.AuthDao
import com.jasmeet.chaintechassignment.model.data.AuthData

@Database(entities = [AuthData::class], version = 1, exportSchema = false)
abstract class AuthDatabase : RoomDatabase() {
    abstract fun authDao(): AuthDao
}