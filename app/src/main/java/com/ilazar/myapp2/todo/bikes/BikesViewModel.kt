package com.ilazar.myapp2.todo.bikes

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilazar.myapp2.core.Result
import com.ilazar.myapp2.core.TAG
import com.ilazar.myapp2.todo.data.Bike
import com.ilazar.myapp2.todo.data.BikeRepository
import com.ilazar.myapp2.todo.data.local.BikeDatabase
import kotlinx.coroutines.launch

class BikeListViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val bikes: LiveData<List<Bike>>
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    private val bikeRepository: BikeRepository

    init {
        val bikeDao = BikeDatabase.getDatabase(application, viewModelScope).bikeDao()
        bikeRepository = BikeRepository(bikeDao)
        bikes = bikeRepository.bikes
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...")
            mutableLoading.value = true
            mutableException.value = null
            when (val result = bikeRepository.refresh()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded")
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception)
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }
}