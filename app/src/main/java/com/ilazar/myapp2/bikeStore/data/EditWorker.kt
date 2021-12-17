package com.ilazar.myapp2.bikeStore.data

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ilazar.myapp2.bikeStore.data.local.BikeDatabase
import com.ilazar.myapp2.bikeStore.data.remote.BikeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditWorker (
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.d("EditWorker","Started")
        val bikeId = inputData.getString("bikeId");
        Log.d("EditWorker","BikeId: $bikeId")

        val bikeDao = BikeDatabase.getDatabase(applicationContext, GlobalScope).bikeDao()
        Log.d("EditWorker",bikeDao.getSize().toString())

        val bike = bikeDao.getByIdNotLiveData(bikeId)
        Log.d("EditWorker", "Returned bike $bike")
        if (bike != null) {
            GlobalScope.launch (Dispatchers.Main) {
                BikeApi.service.update(bike._id, bike)
            }
            Log.d("EditWorker", "Edited bike $bike")
            return Result.success();
        }
        return Result.failure();
    }
}