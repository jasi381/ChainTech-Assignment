package com.jasmeet.chaintechassignment.repository

import com.jasmeet.chaintechassignment.dao.AuthDao
import com.jasmeet.chaintechassignment.model.data.AuthData
import javax.inject.Inject

class AuthDataRepository (private val myDao: AuthDao) {
    suspend fun insert(entity: AuthData) {
        myDao.insert(entity)
    }

    suspend fun delete(entity: AuthData) {
        myDao.delete(entity)
    }

    suspend fun getAll(): List<AuthData> {
        return myDao.getAll()
    }
}
