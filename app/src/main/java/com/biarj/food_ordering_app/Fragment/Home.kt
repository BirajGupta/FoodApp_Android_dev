package com.biarj.food_ordering_app.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.VoiceInteractor
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.biarj.food_ordering_app.Adapter.HomeRecyclerAdapter
import com.biarj.food_ordering_app.R
import com.biarj.food_ordering_app.database.RestaurantDatabase
import com.biarj.food_ordering_app.database.RestaurantEntity
import com.biarj.food_ordering_app.models.Restaurants
import com.biarj.food_ordering_app.util.ConnectionManager
import kotlinx.android.synthetic.main.recycler_home_single_row.*
import org.json.JSONException
import java.util.*
import java.util.Collections.sort
import kotlin.Comparator
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class Home : Fragment() {

    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter : HomeRecyclerAdapter
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var sharedPreferences: SharedPreferences

    val restaurantList = arrayListOf<Restaurants>()

    var ratingComparator = Comparator<Restaurants>{res1, res2 ->

        if(res1.txtRestaurantRating.compareTo(res2.txtRestaurantRating,true) == 0){
            res1.txtRestaurantName.compareTo(res2.txtRestaurantName,true)
        }else {
            res1.txtRestaurantRating.compareTo(res2.txtRestaurantRating, true)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        sharedPreferences = this.getActivity()!!.getSharedPreferences("Food Order", Context.MODE_PRIVATE)

        setHasOptionsMenu(true)

        recyclerHome = view.findViewById(R.id.recyclerHome)

        layoutManager = LinearLayoutManager(activity)

        progressLayout = view.findViewById(R.id.homeProgressLayout)

        progressBar = view.findViewById(R.id.homeProgressBar)

        progressLayout.visibility = View.VISIBLE


        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {
                        progressLayout.visibility = View.GONE
                        val data = it.getJSONObject("data")

                        val success = data.getBoolean("success")

                        if (success) {

                            val array = data.getJSONArray("data")

                            for (i in 0 until array.length()) {

                                val restaurant = array.getJSONObject(i)
                                val restaurantObject = Restaurants(
                                    restaurant.getString("id"),
                                    restaurant.getString("name"),
                                    restaurant.getString("rating"),
                                    restaurant.getString("cost_for_one"),
                                    restaurant.getString("image_url")
                                )

                                restaurantList.add(restaurantObject)

                                if(activity!=null) {
                                    recyclerAdapter =
                                        HomeRecyclerAdapter(
                                            activity as Context,
                                            restaurantList,
                                            sharedPreferences.getString("user_id", "100").toString()
                                        )

                                    recyclerHome.adapter = recyclerAdapter

                                    recyclerHome.layoutManager = layoutManager
                                }

                            }

                        } else {

                            Toast.makeText(
                                activity as Context,
                                "Some Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }catch(e: JSONException){

            Toast.makeText(activity as Context,"Some unexpected error occurred",Toast.LENGTH_SHORT).show()

                }

                }, Response.ErrorListener {
                    if(activity!=null){
                    Toast.makeText(activity as Context,"Volley Error Occurred",Toast.LENGTH_SHORT).show()
                    }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id == R.id.action_sort){
           Collections.sort(restaurantList, ratingComparator)
            restaurantList.reverse()
        }

        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }



}
