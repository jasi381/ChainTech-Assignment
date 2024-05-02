package com.jasmeet.chaintechassignment.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class AuthData(

    val name: String,
    @PrimaryKey()
    val username: String,
    val password: String

)
