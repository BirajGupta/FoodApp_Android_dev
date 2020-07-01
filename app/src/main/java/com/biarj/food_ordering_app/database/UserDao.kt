package com.biarj.food_ordering_app.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith



    @RunWith(AndroidJUnit4::class)
    class ResDaoTest {

        private lateinit var database: RestaurantDatabase
        @Before
        fun initDb(){

            database = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().context,
                RestaurantDatabase::class.java).build()

        }

        @After
        fun closeDb() {
            database.close()
        }



    }

    @RunWith(AndroidJUnit4::class)
    class UserDaoTest {

        @get:Rule
        var instantTaskExectorRule = InstantTaskExecutorRule()

    }

