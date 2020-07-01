package com.biarj.food_ordering_app.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.FtsOptions
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.biarj.food_ordering_app.Adapter.CartRecyclerAdapter
import com.biarj.food_ordering_app.Adapter.FavouriteRecyclerAdapter
import com.biarj.food_ordering_app.Adapter.HomeRecyclerAdapter
import com.biarj.food_ordering_app.Fragment.FavouriteFragment
import com.biarj.food_ordering_app.R
import com.biarj.food_ordering_app.database.CartOrders
import com.biarj.food_ordering_app.database.MIGRATION_1_2
import com.biarj.food_ordering_app.database.RestaurantDatabase
import com.biarj.food_ordering_app.database.RestaurantEntity
import com.biarj.food_ordering_app.models.Restaurants
import kotlinx.android.synthetic.main.activity_after_login.*
import kotlinx.android.synthetic.main.activity_restaurant_details.*
import kotlinx.android.synthetic.main.activity_restaurant_details.toolbar
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartPageActivity : AppCompatActivity() {

    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var recyclerOrders : RecyclerView
    lateinit var recyclerAdapter : CartRecyclerAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var btnPlaceOrder: Button
    lateinit var txtResName : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_page)

        sharedPreferences = getSharedPreferences("Food Order", Context.MODE_PRIVATE)

        setUpToolbar()

        val resId = sharedPreferences.getString("resId", "100")
        println(resId)

        val userId = sharedPreferences.getString("user_id", "100")

        txtResName = findViewById(R.id.txtResName)

        layoutManager = LinearLayoutManager(this@CartPageActivity)

        val orders: List<CartOrders> =
            RetrieveOrders(this@CartPageActivity, resId.toString()).execute().get()

        txtResName.text = orders[0].resName
        var totalCost: Int = 0
        val foodItemsArray = JSONArray()

        for(i in orders){

            totalCost += i.price.toInt()
            val obj = JSONObject()
            obj.put("food_item_id",i.foodId.toString())
            foodItemsArray.put(obj)
        }

        recyclerOrders = findViewById(R.id.recyclerOrders)
        recyclerAdapter = CartRecyclerAdapter(this@CartPageActivity, orders)
        recyclerOrders.layoutManager = layoutManager
        recyclerOrders.adapter = recyclerAdapter



        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)

        btnPlaceOrder.text = "Place Order(Total: Rs. $totalCost)"

        val queue = Volley.newRequestQueue(this@CartPageActivity)

        val url = "http://13.235.250.119/v2/place_order/fetch_result/"

        val jsonParams = JSONObject()
        jsonParams.put("user_id", userId)
        jsonParams.put("restaurant_id", resId)
        jsonParams.put("total_cost",totalCost.toString())
        jsonParams.put("food",foodItemsArray)
        println(jsonParams)

        btnPlaceOrder.setOnClickListener {

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    try {

                        val success = it.getJSONObject("data").getBoolean("success")

                        if (success) {

                            val intent = Intent(this@CartPageActivity, ConfirmNotification :: class.java)
                            startActivity(intent)
                            finish()

                        } else {

                            Toast.makeText(
                                this@CartPageActivity,
                                "Some Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    } catch (e: JSONException) {

                        Toast.makeText(
                            this@CartPageActivity,
                            "Some unexpected error occurred",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }, Response.ErrorListener {

                    Toast.makeText(
                        this@CartPageActivity,
                        "Volley Error Occurred",
                        Toast.LENGTH_SHORT
                    ).show()

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "8cac724ac3760c"

                        return headers
                    }

                }
            queue.add(jsonObjectRequest)

        }

    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    class RetrieveOrders(val context: Context, val resId : String): AsyncTask<Void, Void, List<CartOrders>>(){


        override fun doInBackground(vararg params: Void?): List<CartOrders> {

            val db2 = Room.databaseBuilder(context, RestaurantDatabase :: class.java, "CartOrders")
                .addMigrations(MIGRATION_1_2).build()


            return db2.resDao().getAllOrders(resId.toInt())
        }


    }
}
