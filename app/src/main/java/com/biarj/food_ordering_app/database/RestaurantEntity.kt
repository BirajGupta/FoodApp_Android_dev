package com.biarj.food_ordering_app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Restaurants")
data class RestaurantEntity(
    @PrimaryKey val res_id:Int,
    @ColumnInfo(name = "resName")val resName : String,
    @ColumnInfo(name = "resPrice")val resPrice : String,
    @ColumnInfo(name = "resRating")val resRating : String,
    @ColumnInfo(name = "resImage")val resImage : String
)

