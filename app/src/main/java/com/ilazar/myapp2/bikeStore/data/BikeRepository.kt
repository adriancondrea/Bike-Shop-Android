package com.ilazar.myapp2.bikeStore.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.*
import com.ilazar.myapp2.core.Result
import com.ilazar.myapp2.bikeStore.data.local.BikeDao
import com.ilazar.myapp2.bikeStore.data.remote.BikeApi

class BikeRepository(private val bikeDao: BikeDao) {

    val bikes = bikeDao.getAll()

    suspend fun refresh(): Result<Boolean> {
        return try {
            bikeDao.deleteAll()
            val bikes = BikeApi.service.find()
            for (bike in bikes) {
                bikeDao.insert(bike)
            }
            Result.Success(true)
        } catch(e: Exception) {
            Result.Error(e)
        }
    }

    fun getById(bikeId: String): LiveData<Bike> {
        return bikeDao.getById(bikeId)
    }

    suspend fun save(bike: Bike): Result<Bike> {
        return try {
            val createdBike = BikeApi.service.create(bike)
            bikeDao.insert(createdBike)
            Result.Success(createdBike)
        } catch(e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun update(bike: Bike): Result<Bike> {
        return try {
            val updatedBike = BikeApi.service.update(bike._id, bike)
            bikeDao.update(updatedBike)
            Result.Success(updatedBike)
        } catch(e: Exception) {
            Log.d("edit","failed to edit on server")
            bikeDao.update(bike)
            Log.d("edit","edited locally id ${bike._id}")
            startEditJob(bike._id)
            Log.d("edit","enqueued job")
            Result.Error(e)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun startEditJob(bikeId: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val inputData = Data.Builder()
            .put("bikeId",bikeId)
            .build()
        val myWork = OneTimeWorkRequest.Builder(EditWorker::class.java)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()
        WorkManager.getInstance().enqueue(myWork)
    }
}