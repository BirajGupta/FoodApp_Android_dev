package com.biarj.food_ordering_app.Fragment

import android.app.Application
import android.content.Context
import android.content.RestrictionEntry
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.biarj.food_ordering_app.Adapter.FavouriteRecyclerAdapter
import com.biarj.food_ordering_app.R
import com.biarj.food_ordering_app.database.*

/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : Fragment() {
    val sharedPreferences = this.activity?.getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
    lateinit var recyclerFavourite : RecyclerView
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var recyclerAdapter : FavouriteRecyclerAdapter
    var dbResList = listOf<RestaurantEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_favourite, container, false)

        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)
        progressLayout = view.findViewById(R.id.favouriteProgressLayout)
        progressBar = view.findViewById(R.id.favouriteProgressBar)

        layoutManager = LinearLayoutManager(activity)

        dbResList = RetrieveFavourites(activity as Context).execute().get()

        if(activity != null){
            progressLayout.visibility = View.GONE
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, dbResList)
            recyclerFavourite.adapter = recyclerAdapter                   /*lines responsible for linking the adapter and layout manager to*/
            recyclerFavourite.layoutManager = layoutManager                        /* to recyclerview in xml file*/
        }

        return view
    }


    class RetrieveFavourites(val context: Context): AsyncTask<Void, Void, List<RestaurantEntity>>(){

        override fun doInBackground(vararg params: Void?): List<RestaurantEntity>? {

            val db1 = Room.databaseBuilder(context, RestaurantDatabase::class.java, "Restaurants")
                .addMigrations(MIGRATION_1_2).build()


            return db1.resDao().getAllRestaurants()

        }

    }

}
