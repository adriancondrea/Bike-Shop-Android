package com.ilazar.myapp2

import android.util.Log

object ItemRepository {
    suspend fun getAll(): List<Item> {
        Log.i(TAG, "getAll");
        return ItemApi.service.getAll()
    }
}