package com.ilazar.myapp2.bikeStore.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bikes")
data class Bike(
    @PrimaryKey @ColumnInfo(name = "_id") var _id: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "condition") var condition: String,
    @ColumnInfo(name = "warranty") var warranty: Boolean,
    @ColumnInfo(name = "price") var price: Int
) {
    override fun toString(): String {
        return "Bike(_id='$_id', name='$name', condition='$condition', warranty=$warranty, price=$price)"
    }
}
