package com.ilazar.myapp2.bikeStore.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ilazar.myapp2.bikeStore.data.Bike

@Dao
interface BikeDao {
    @Query("SELECT * from bikes ORDER BY name ASC")
    fun getAll(): LiveData<List<Bike>>

    @Query("SELECT * FROM bikes WHERE _id=:id ")
    fun getById(id: String): LiveData<Bike>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bike: Bike)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(bike: Bike)

    @Query("DELETE FROM bikes")
    suspend fun deleteAll()

    @Query("SELECT Count(*) FROM bikes")
    fun getSize(): Int

    @Query("SELECT * FROM bikes WHERE _id=:id ")
    fun getByIdNotLiveData(id: String?): Bike
}