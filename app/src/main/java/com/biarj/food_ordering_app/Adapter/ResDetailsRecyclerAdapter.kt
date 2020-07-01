package com.biarj.food_ordering_app.Adapter

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color.rgb
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.biarj.food_ordering_app.Activity.RestaurantDetailsActivity
import com.biarj.food_ordering_app.R
import com.biarj.food_ordering_app.R.string.preferences_file_name
import com.biarj.food_ordering_app.database.CartOrders
import com.biarj.food_ordering_app.database.MIGRATION_1_2
import com.biarj.food_ordering_app.database.RestaurantDatabase
import com.biarj.food_ordering_app.database.RestaurantEntity
import com.biarj.food_ordering_app.models.RestaurantMenu


class ResDetailsRecyclerAdapter (val context: Context, val itemList: ArrayList<RestaurantMenu>) : RecyclerView.Adapter<ResDetailsRecyclerAdapter.ResDetailsViewHolder>() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResDetailsViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_details_single_row,parent, false)


        return ResDetailsViewHolder(view)

    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    override fun onBindViewHolder(
        holder: ResDetailsViewHolder,
        position: Int
    ) {
        val menuItem = itemList[position]
        holder.menuItemName.text = menuItem.food_name
        holder.menuItemNumber.text = menuItem.food_id.toString()
        holder.menuItemPrice.text = "Rs.${menuItem.cost}"

        sharedPreferences = this.context.getSharedPreferences("Food Order",Context.MODE_PRIVATE)

        val resName = sharedPreferences.getString("resName","nothing")

        val cartFoodItem = CartOrders(
            menuItem.restaurant_id.toInt(),
            resName.toString(),
            menuItem.food_id,
            menuItem.food_name,
            menuItem.cost
        )

        val checkOrder = FoodOrders(context, cartFoodItem, 1).execute()
        val isPresent = checkOrder.get()

        if(isPresent){
            holder.menuItemAdd.text = context.getString(R.string.remove)
            holder.menuItemAdd.setBackgroundColor(rgb(247, 146, 45))
        }else{
            holder.menuItemAdd.text = context.getString(R.string.add)
            holder.menuItemAdd.setBackgroundColor(rgb(76, 140,74))
        }

        holder.menuItemAdd.setOnClickListener{
            println(cartFoodItem)
            if(!FoodOrders(context, cartFoodItem, 1).execute().get()){
                val async = FoodOrders(context, cartFoodItem, 2).execute()
                val result = async.get()

                if(result){

                    Toast.makeText(context,"added", Toast.LENGTH_SHORT).show()
                    holder.menuItemAdd.setBackgroundColor(rgb(247, 146, 45))

                    holder.menuItemAdd.text = context.getString(R.string.remove)
                }else{
                    Toast.makeText(context,"Some error occurred while adding", Toast.LENGTH_SHORT).show()
                }
            }else{
                val async = FoodOrders(context, cartFoodItem, 3).execute()
                val result = async.get()

                if(result){
                    Toast.makeText(context,"removed", Toast.LENGTH_SHORT).show()
                    holder.menuItemAdd.text = context.getString(R.string.add)
                    holder.menuItemAdd.setBackgroundColor(rgb(76, 140,74))
                }else{
                    Toast.makeText(context,"Error occurred while removing", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }


    class ResDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val menuItemNumber : TextView = view.findViewById(R.id.txtMenuItemNumber)
        val menuItemName : TextView = view.findViewById(R.id.txtMenuItemName)
        val menuItemPrice : TextView = view.findViewById(R.id.txtMenuItemPrice)
        val menuItemAdd : Button = view.findViewById(R.id.btnMenuItemAdd)

    }

    class FoodOrders(val context: Context, val cartFoodItem: CartOrders, val mode: Int) : AsyncTask<Void, Void, Boolean>(){

        val db2 = Room.databaseBuilder(context, RestaurantDatabase::class.java,"CartOrders").addMigrations(
            MIGRATION_1_2
        ).build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){

                1 ->{
                    val order: CartOrders? = db2.resDao().getFoodItem(cartFoodItem.foodName)
                    db2.close()
                    return order != null
                }

                2 -> {
                    db2.resDao().addOrder(cartFoodItem)
                    println(cartFoodItem)
                    db2.close()
                    return true
                }

                3 -> {
                    db2.resDao().removeOrder(cartFoodItem)
                    db2.close()
                    return true
                }

            }

            return false

        }


    }

}