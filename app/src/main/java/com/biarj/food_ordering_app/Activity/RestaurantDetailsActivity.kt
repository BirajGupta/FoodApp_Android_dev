package com.biarj.food_ordering_app.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.biarj.food_ordering_app.Adapter.HomeRecyclerAdapter
import com.biarj.food_ordering_app.Adapter.ResDetailsRecyclerAdapter
import com.biarj.food_ordering_app.Fragment.Home
import com.biarj.food_ordering_app.R
import com.biarj.food_ordering_app.database.CartOrders
import com.biarj.food_ordering_app.database.MIGRATION_1_2
import com.biarj.food_ordering_app.database.RestaurantDatabase
import com.biarj.food_ordering_app.models.RestaurantMenu
import kotlinx.android.synthetic.main.activity_restaurant_details.*
import org.json.JSONException

class RestaurantDetailsActivity : AppCompatActivity() {

    lateinit var recyclerResDetails: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter : ResDetailsRecyclerAdapter
    lateinit var proceedToCart : Button
    lateinit var sharedPreferences: SharedPreferences

    val menuItems = arrayListOf<RestaurantMenu>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)

        sharedPreferences = getSharedPreferences("Food Order", Context.MODE_PRIVATE)

        setUpToolbar()

        recyclerResDetails = findViewById(R.id.recyclerResDetails)
        proceedToCart = findViewById(R.id.btnProceedToCart)
        layoutManager = LinearLayoutManager(this@RestaurantDetailsActivity)

        sharedPreferences = getSharedPreferences("Food Order", Context.MODE_PRIVATE)

        val resId = sharedPreferences.getString("resId", "100")

        val queue = Volley.newRequestQueue(this@RestaurantDetailsActivity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$resId"

        val jsonObjectRequest =
            object : JsonObjectRequest(Method.GET, url, null, Response.Listener {

                try {

                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")

                    when (success) {

                        true -> {

                            val resMenu = data.getJSONArray("data")

                            for (i in 0 until resMenu.length()) {

                                val menuItem = resMenu.getJSONObject(i)
                                val item = RestaurantMenu(
                                    i + 1,
                                    menuItem.getString("name"),
                                    menuItem.getString("cost_for_one"),
                                    menuItem.getString("restaurant_id")
                                )

                                menuItems.add(item)

                                recyclerAdapter = ResDetailsRecyclerAdapter(
                                    this@RestaurantDetailsActivity,
                                    menuItems
                                )

                                recyclerResDetails.adapter = recyclerAdapter

                                recyclerResDetails.layoutManager = layoutManager

                            }

                        }

                        false -> {

                            Toast.makeText(
                                this@RestaurantDetailsActivity,
                                "Some Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }

                } catch (e: JSONException) {

                    Toast.makeText(
                        this@RestaurantDetailsActivity,
                        "Some error Occurred",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }, Response.ErrorListener {

                Toast.makeText(this@RestaurantDetailsActivity, "hi", Toast.LENGTH_LONG).show()

            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "8cac724ac3760c"

                    return headers
                }
            }

        queue.add(jsonObjectRequest)


        proceedToCart.setOnClickListener {
                val intent = Intent(this@RestaurantDetailsActivity, CartPageActivity::class.java)
                startActivity(intent)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id == android.R.id.home){

            val dialog = AlertDialog.Builder(this@RestaurantDetailsActivity)
            dialog.setTitle("Confirmation")
            dialog.setMessage("Going back will reset items. Do you still want to proceed?")
            dialog.setPositiveButton("YES"){text,listener->

                DeleteAll(this@RestaurantDetailsActivity,1).execute()

                val intent = Intent(this@RestaurantDetailsActivity, AfterLoginActivity :: class.java)
                startActivity(intent)
                finish()
            }
            dialog.setNegativeButton("NO"){text,listener->

            }
            dialog.create()
            dialog.show()

        }

        return super.onOptionsItemSelected(item)
    }


    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = sharedPreferences.getString("resName","Restaurant details")
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed(){

        val dialog = AlertDialog.Builder(this@RestaurantDetailsActivity)
        dialog.setTitle("Confirmation")
        dialog.setMessage("Going back will reset items. Do you still want to proceed?")
        dialog.setPositiveButton("YES"){text,listener->

            DeleteAll(this@RestaurantDetailsActivity, 1).execute()

            val intent = Intent(this@RestaurantDetailsActivity, AfterLoginActivity :: class.java)
            startActivity(intent)
            finish()
        }
        dialog.setNegativeButton("NO"){text,listener->

        }
        dialog.create()
        dialog.show()

    }


    class DeleteAll(val context: Context, val mode : Int) : AsyncTask<Void, Void, Boolean>() {



        val db2 = Room.databaseBuilder(context, RestaurantDatabase::class.java, "CartOrders")
            .addMigrations(
                MIGRATION_1_2
            ).build()

        override fun doInBackground(vararg params: Void?): Boolean {


                    db2.resDao().deleteAllEntries()
                    db2.close()
                    return true
                }
    }

    }


