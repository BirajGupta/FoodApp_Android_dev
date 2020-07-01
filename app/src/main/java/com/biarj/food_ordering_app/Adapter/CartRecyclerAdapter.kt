package com.biarj.food_ordering_app.Adapter

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.biarj.food_ordering_app.Activity.CartPageActivity
import com.biarj.food_ordering_app.R
import com.biarj.food_ordering_app.database.CartOrders
import kotlinx.android.synthetic.main.recycler_cart_single_row.view.*

class CartRecyclerAdapter(val context: Context, val foodOrderList : List<CartOrders>) : RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_cart_single_row, parent, false)

            return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        println(foodOrderList.size)
        return foodOrderList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        val item = foodOrderList[position]

        holder.foodName.text = item.foodName
        holder.foodPrice.text = "Rs.${item.price}"

    }

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view){

        var foodName : TextView = view.findViewById(R.id.txtFoodItemName)
        var foodPrice : TextView = view.findViewById(R.id.txtFoodItemPrice)

    }


}