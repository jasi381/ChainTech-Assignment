package com.jasmeet.chaintechassignment.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class AuthData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val username: String,
    val password: String

)
