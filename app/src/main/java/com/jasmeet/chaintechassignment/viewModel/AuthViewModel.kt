package com.jasmeet.chaintechassignment.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasmeet.chaintechassignment.model.data.AuthData
import com.jasmeet.chaintechassignment.repository.AuthDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthDataRepository,
) : ViewModel() {

    private val _allAuthData = MutableLiveData<List<AuthData>>()
    val allAuthData: LiveData<List<AuthData>> = _allAuthData


    init {
        getAllData()
    }

    fun getAllData(){
        viewModelScope.launch {
            try {
                _allAuthData.value = repository.getAll()
                Log.d("vm",_allAuthData.value.toString())
            } catch (e: Exception) {
                Log.e("MyViewModel", "Error getting data: ${e.message}")
            }
        }
    }

    fun insert(entity: AuthData) {
        viewModelScope.launch {
            repository.insert(entity)
            getAllData()
        }
    }

    fun update(entity: AuthData) {
        viewModelScope.launch {
            repository.update(entity)
        }
    }

    fun delete(entity: AuthData) {
        viewModelScope.launch {
            repository.delete(entity)
            getAllData()
        }
    }


}
