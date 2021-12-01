package com.ilazar.myapp2.todo.data

import androidx.lifecycle.LiveData
import com.ilazar.myapp2.core.Result
import com.ilazar.myapp2.todo.data.local.BikeDao
import com.ilazar.myapp2.todo.data.remote.BikeApi

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
            Result.Error(e)
        }
    }
}