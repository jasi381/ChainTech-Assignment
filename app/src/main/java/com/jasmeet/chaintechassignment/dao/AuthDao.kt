package com.jasmeet.chaintechassignment.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jasmeet.chaintechassignment.model.data.AuthData

@Dao
interface AuthDao {
    @Insert
    suspend fun insert(entity: AuthData)

    @Update
    suspend fun update(entity: AuthData)

    @Delete
    suspend fun delete(entity: AuthData)

    @Query("SELECT * FROM user_data")
    suspend fun getAll(): List<AuthData>
}