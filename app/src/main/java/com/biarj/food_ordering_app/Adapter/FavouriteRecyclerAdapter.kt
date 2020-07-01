package com.biarj.food_ordering_app.Adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.biarj.food_ordering_app.Activity.RestaurantDetailsActivity
import com.biarj.food_ordering_app.R
import com.biarj.food_ordering_app.database.RestaurantEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context, val restaurantList: List<RestaurantEntity>) : RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>(){

    lateinit var sharedPreferences: SharedPreferences

    class FavouriteViewHolder(view : View): RecyclerView.ViewHolder(view){
        val restaurantImage : ImageView = view.findViewById(R.id.imgFavRestaurantImage)
        val restaurantName: TextView = view.findViewById(R.id.txtFavHomeName)
        val restaurantPrice: TextView = view.findViewById(R.id.txtFavHomePrice)
        val restaurantRating: TextView = view.findViewById(R.id.txtFavHomeRatiing)
        val restaurantFavourite : ImageView = view.findViewById(R.id.txtFavFavourite)
        val restaurantLayout : LinearLayout = view.findViewById(R.id.llFavContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favourite_single_row,parent,false)

        return FavouriteViewHolder(view)

    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {

        sharedPreferences = this.context.getSharedPreferences("Food Order",Context.MODE_PRIVATE)
        val res = restaurantList[position]
        holder.restaurantName.text = res.resName
        holder.restaurantPrice.text = res.resPrice
        holder.restaurantRating.text = res.resRating
        Picasso.get().load(res.resImage).error(R.drawable.default_book_cover).into(holder.restaurantImage)

        holder.restaurantLayout.setOnClickListener{

            sharedPreferences.edit().putString("resName",res.resName).apply()
            sharedPreferences.edit().putString("resId",res.res_id.toString()).apply()
            val intent = Intent(context, RestaurantDetailsActivity::class.java)
            context.startActivity(intent)

        }

    }

}