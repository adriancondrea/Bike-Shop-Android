package com.ilazar.myapp2.todo.bike

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

class BikeEditViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    private val bikeRepository: BikeRepository

    init {
        val bikeDao = BikeDatabase.getDatabase(application, viewModelScope).bikeDao()
        bikeRepository = BikeRepository(bikeDao)
    }

    fun getBikeById(bikeId: String): LiveData<Bike> {
        Log.v(TAG, "getBikeById...")
        return bikeRepository.getById(bikeId)
    }

    fun saveOrUpdateBike(bike: Bike) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateBike...")
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Bike> = if (bike._id.isNotEmpty()) {
                bikeRepository.update(bike)
            } else {
//                bike._id = (bikeRepository.bikes.value?.size?.plus(1)).toString()
                bikeRepository.save(bike)
            }
            when(result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateBike succeeded")
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateBike failed", result.exception)
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}