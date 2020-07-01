package com.biarj.food_ordering_app.Adapter

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biarj.food_ordering_app.R
import com.biarj.food_ordering_app.database.CartOrders
import com.biarj.food_ordering_app.models.Orders
import kotlinx.android.synthetic.main.recycler_history_single_row.view.*

class HistoryRecyclerAdapter(val context: Context, val info : ArrayList<Orders>, val order : ArrayList<CartOrders>) : RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_history_single_row, parent, false)


        return HistoryViewHolder(view,order)

    }

    override fun getItemCount(): Int {
        return info.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = info[position]

        holder.name.text = item.resName
        holder.date.text = item.date

        holder.recyclerOrders.layoutManager = holder.layoutManager
        holder.recyclerOrders.adapter = holder.recyclerAdapter
    }



    class HistoryViewHolder(view: View, var order: ArrayList<CartOrders>) : RecyclerView.ViewHolder(view){

        var name : TextView = view.findViewById(R.id.txtHistoryResName)
        var date : TextView = view.findViewById(R.id.txtHistoryDate)
        var recyclerOrders : RecyclerView = view.findViewById(R.id.recyclerOrders)
        var layoutManager = LinearLayoutManager(Application())
        var recyclerAdapter = CartRecyclerAdapter(Application(), order)

    }


}