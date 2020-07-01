package com.biarj.food_ordering_app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "FavUserRes")
data class FavUserRestaurants(
    @PrimaryKey val res_id:String,
    @ColumnInfo(name = "userId")val user_id:String
)