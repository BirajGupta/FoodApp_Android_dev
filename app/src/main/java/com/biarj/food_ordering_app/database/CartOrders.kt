package com.biarj.food_ordering_app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.w3c.dom.Text


@Entity(tableName = "CartOrders")
data class CartOrders (

    @ColumnInfo(name = "resId")val resId : Int,
    @ColumnInfo(name = "resName") val resName : String,
    @ColumnInfo (name = "foodId") val foodId : Int,
    @PrimaryKey val foodName : String,
    @ColumnInfo(name = "price") val price : String

    )