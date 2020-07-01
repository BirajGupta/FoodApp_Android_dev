package com.biarj.food_ordering_app.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.biarj.food_ordering_app.R

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
        setContentView(R.layout.activity_main)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)

        if(isLoggedIn){
            val intent = Intent(this@MainActivity, AfterLoginActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Handler().postDelayed({
                val startAct = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(startAct)
                finish()
            },2000)
        }


    }



}



