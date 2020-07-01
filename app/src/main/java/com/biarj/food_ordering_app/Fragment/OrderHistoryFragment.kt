package com.biarj.food_ordering_app.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.biarj.food_ordering_app.Adapter.HistoryRecyclerAdapter

import com.biarj.food_ordering_app.R
import com.biarj.food_ordering_app.database.CartOrders
import com.biarj.food_ordering_app.models.Orders
import com.biarj.food_ordering_app.util.ConnectionManager
import org.json.JSONException

/**
 * A simple [Fragment] subclass.
 */
class OrderHistoryFragment : Fragment() {

    lateinit var layoutManagerHistory : RecyclerView.LayoutManager
    lateinit var recyclerOrdersHistory : RecyclerView
    lateinit var recyclerAdapterHistory : HistoryRecyclerAdapter
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)

        sharedPreferences = this.activity!!.getSharedPreferences("Food Order", Context.MODE_PRIVATE)

        val resId  = sharedPreferences.getString("resId", "100")!!.toInt()

        val userId = sharedPreferences.getString("user_id", "100")
        println("111111111")
        println(userId)

        val orders: ArrayList<CartOrders> = arrayListOf()
        val info : ArrayList<Orders> = arrayListOf()

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v2/orders/fetch_result/$userId"


        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {

                        val data = it.getJSONObject("data")

                        val success = data.getBoolean("success")

                        if (success) {

                            val ordersArray = it.getJSONObject("data").getJSONArray("data")

                            for (i in 0 until ordersArray.length()){

                                val ele = ordersArray.getJSONObject(i).getJSONArray("food_items")

                                for(j in 0 until ele.length()) {

                                    val order = CartOrders(
                                        resId,
                                        ordersArray.getJSONObject(i).getString("restaurant_name"),
                                        ele.getJSONObject(j).getString("food_item_id").toInt(),
                                        ele.getJSONObject(j).getString("name"),
                                        ele.getJSONObject(j).getString("cost")
                                    )

                                    orders.add(order)
                                }

                                val information = Orders(
                                    ordersArray.getJSONObject(i).getString("restaurant_name"),
                                    ordersArray.getJSONObject(i).getString("order_placed_at")
                                )

                                info.add(information)
                                println(info)

                            }

                            layoutManagerHistory = LinearLayoutManager(activity as Context)
                            recyclerOrdersHistory = view.findViewById(R.id.recyclerHistory)
                            recyclerAdapterHistory = HistoryRecyclerAdapter(activity as Context, info, orders)
                            recyclerOrdersHistory.layoutManager = layoutManagerHistory
                            recyclerOrdersHistory.adapter = recyclerAdapterHistory


                        } else {

                            Toast.makeText(
                                activity as Context,
                                "Some Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }catch(e: JSONException){

                        Toast.makeText(activity as Context,"Some unexpected error occurred", Toast.LENGTH_SHORT).show()
                        println("44444444")
                        println(e)

                    }

                }, Response.ErrorListener {
                    Toast.makeText(activity as Context,"Volley Error Occurred", Toast.LENGTH_SHORT).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "8cac724ac3760c"

                        return headers
                    }
                }

            queue.add(jsonObjectRequest)
        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("error")
            dialog.setMessage("Internet Conn not found")
            dialog.setPositiveButton("Open Settings"){text,listener->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

}
