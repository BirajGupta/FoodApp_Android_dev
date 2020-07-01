package com.biarj.food_ordering_app.Adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color.rgb
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.biarj.food_ordering_app.Activity.RestaurantDetailsActivity
import com.biarj.food_ordering_app.R
import com.biarj.food_ordering_app.database.FavUserRestaurants
import com.biarj.food_ordering_app.database.MIGRATION_1_2
import com.biarj.food_ordering_app.database.RestaurantDatabase
import com.biarj.food_ordering_app.database.RestaurantEntity
import com.biarj.food_ordering_app.models.Restaurants
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context: Context, val itemList: ArrayList<Restaurants>, val userId : String) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>(){

    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
           val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycler_home_single_row,parent, false)

        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant = itemList[position]
        holder.restaurantName.text = restaurant.txtRestaurantName
        holder.restaurantPrice.text = "Rs.${restaurant.txtRestaurantPrice}/Preson"
        holder.restaurantRating.text = restaurant.txtRestaurantRating

        sharedPreferences = this.context.getSharedPreferences("Food Order",Context.MODE_PRIVATE)

        sharedPreferences.edit().putString("resName", "nothing").apply()

        Picasso.get().load(restaurant.imgRestaurantImage).error(R.drawable.default_book_cover).into(holder.restaurantImage);

        val restaurantEntityVar = RestaurantEntity(
            restaurant.txtRestaurantId.toInt(),
            restaurant.txtRestaurantName,
            restaurant.txtRestaurantPrice,
            restaurant.txtRestaurantRating,
            restaurant.imgRestaurantImage
        )

         val checkFav = DBAsyncTask(context, restaurantEntityVar, 1).execute()
         val isFav = checkFav.get()

        if(isFav){
            holder.restaurantFavourite.setImageResource(R.drawable.ic_newheart_foreground)
        }else{
            holder.restaurantFavourite.setImageResource(R.drawable.ic_heart_foreground)
        }

        holder.restaurantFavourite.setOnClickListener{
            if(!DBAsyncTask(context, restaurantEntityVar,1).execute().get()){
                val async = DBAsyncTask(context, restaurantEntityVar, 2).execute()
                val result = async.get()

                if(result){
                    Toast.makeText(context,"added",Toast.LENGTH_SHORT).show()
                    holder.restaurantFavourite.setImageResource(R.drawable.ic_newheart_foreground)
                }else{
                    Toast.makeText(context,"Some error occurred while adding",Toast.LENGTH_SHORT).show()
                }
            }else{
                val async = DBAsyncTask(context, restaurantEntityVar, 3).execute()
                val result = async.get()

                if(result){
                    Toast.makeText(context,"removed",Toast.LENGTH_SHORT).show()
                    holder.restaurantFavourite.setImageResource(R.drawable.ic_heart_foreground)
                }else{
                    Toast.makeText(context,"Error occurred while removing",Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.restaurantLayout.setOnClickListener{

            sharedPreferences.edit().putString("resName",restaurant.txtRestaurantName).apply()
            sharedPreferences.edit().putString("resId",restaurant.txtRestaurantId).apply()
            val intent = Intent(context, RestaurantDetailsActivity::class.java)
            context.startActivity(intent)

        }

    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val restaurantImage : ImageView = view.findViewById(R.id.imgRestaurantImage)
        val restaurantName:TextView = view.findViewById(R.id.txtHomeName)
        val restaurantPrice:TextView = view.findViewById(R.id.txtHomePrice)
        val restaurantRating:TextView = view.findViewById(R.id.txtHomeRatiing)
        val restaurantFavourite : ImageView = view.findViewById(R.id.imgFavourite)
        val restaurantLayout : LinearLayout = view.findViewById(R.id.llResContent)
    }

    class DBAsyncTask(val context: Context, val RestaurantEntity : RestaurantEntity, val mode :Int): AsyncTask<Void, Void, Boolean>(){

        val db1 = Room.databaseBuilder(context, RestaurantDatabase::class.java,"Restaurants").addMigrations(MIGRATION_1_2).build()


        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){

                1 ->{
                    val res: RestaurantEntity? = db1.resDao().getRestaurant(RestaurantEntity.res_id.toString())
                    db1.close()
                    return res != null
                }

                2 -> {
                    db1.resDao().insertRes(RestaurantEntity)
                    db1.close()
                    return true
                }

                3 -> {
                    db1.resDao().deleteRes(RestaurantEntity)
                    db1.close()
                    return true
                }

            }

            return false
        }
    }

}