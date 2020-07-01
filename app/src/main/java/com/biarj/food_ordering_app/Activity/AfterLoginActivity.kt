package com.biarj.food_ordering_app.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.biarj.food_ordering_app.*
import com.biarj.food_ordering_app.Fragment.*
import com.google.android.material.navigation.NavigationView

class AfterLoginActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var frameLayout: FrameLayout
    lateinit var drawerLayout : DrawerLayout
    lateinit var navigationView : NavigationView
    lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login)

        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name),Context.MODE_PRIVATE)
        frameLayout =findViewById(R.id.frame)
        navigationView =findViewById(R.id.navigationBar)
        toolbar =findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawerLayout)


        initialisehome()

        var previousMenuItem:MenuItem? = null


        setUpToolbar()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@AfterLoginActivity,
            drawerLayout,
            R.string.opendrawer,
            R.string.closedrawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked  = true
            previousMenuItem = it

            when(it.itemId){
                R.id.home -> {
                    initialisehome()
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            ProfileFragment()
                        )
                        .commit()
                    supportActionBar?.title = "My Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.favourites -> {

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FavouriteFragment()
                        )
                        .commit()
                    supportActionBar?.title = "Favourite Restaurants"
                    drawerLayout.closeDrawers()
                }

                R.id.questions -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            Questions()
                        )
                        .commit()
                    supportActionBar?.title = "Frequently Asked Questions"
                    drawerLayout.closeDrawers()
                }
                R.id.logout -> {
                    val dialog = AlertDialog.Builder(this@AfterLoginActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want exit")
                    dialog.setPositiveButton("YES"){text,listener->
                        sharedPreferences.edit().clear().apply()
                        val intent = Intent(this@AfterLoginActivity, LoginActivity :: class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialog.setNegativeButton("No"){text,listener->

                    }
                    dialog.create()
                    dialog.show()
                    drawerLayout.closeDrawers()
                }

                R.id.orderHistory -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            OrderHistoryFragment()
                        )
                        .commit()
                    supportActionBar?.title = "My Previous Orders"
                    drawerLayout.closeDrawers()
                }

            }

            return@setNavigationItemSelectedListener true
        }


    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "All Restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    fun initialisehome(){
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame,
                Home()
            )
            .commit()
        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed(){

        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when(frag){
            !is Home -> initialisehome()
            else -> super.onBackPressed()
        }
    }


}
