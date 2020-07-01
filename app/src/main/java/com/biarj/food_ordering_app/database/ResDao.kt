package com.biarj.food_ordering_app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.biarj.food_ordering_app.models.Restaurants

@Dao
interface ResDao {

    @Insert
    fun insertRes(restaurant : RestaurantEntity)

    @Delete
    fun deleteRes(restaurant: RestaurantEntity)

    @Query("SELECT * FROM Restaurants ")
    fun getAllRestaurants() : List<RestaurantEntity>

    @Query("SELECT * FROM Restaurants WHERE res_id = :resId")
    fun getRestaurant(resId : String): RestaurantEntity

    @Query("SELECT * FROM CartOrders WHERE foodName = :foodName")
    fun getFoodItem(foodName : String) : CartOrders

    @Insert
    fun addOrder(foodItem : CartOrders)

    @Delete
    fun removeOrder(foodItem: CartOrders)

    @Query("SELECT * FROM CartOrders WHERE resId = :resId")
    fun getAllOrders(resId: Int) : List<CartOrders>

    @Query("DELETE FROM CartOrders")
    fun deleteAllEntries()
}

