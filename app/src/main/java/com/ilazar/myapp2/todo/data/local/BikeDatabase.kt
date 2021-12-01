package com.ilazar.myapp2.todo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ilazar.myapp2.todo.data.Bike
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Bike::class], version = 1)
abstract class BikeDatabase : RoomDatabase() {

    abstract fun bikeDao(): BikeDao

    companion object {
        @Volatile
        private var INSTANCE: BikeDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BikeDatabase {
            val inst = INSTANCE
            if (inst != null) {
                return inst
            }
            val instance =
                Room.databaseBuilder(
                    context.applicationContext,
                    BikeDatabase::class.java,
                    "bike_db"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
            INSTANCE = instance
            return instance
        }

        private class WordDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.bikeDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(bikeDao: BikeDao) {
//            itemDao.deleteAll()
//            val item = Item("1", "Hello")
//            itemDao.insert(item)
        }
    }

}
