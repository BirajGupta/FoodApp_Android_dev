package com.biarj.food_ordering_app.database


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = arrayOf(RestaurantEntity :: class, CartOrders :: class), version = 2)
abstract class RestaurantDatabase : RoomDatabase() {

    abstract fun resDao() : ResDao



    }

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE CartOrders (price TEXT NOT NULL, resId INTEGER NOT NULL, resName TEXT NOT NULL, foodId INTEGER NOT NULL, foodName TEXT PRIMARY KEY NOT NULL)"
        )
    }
}





