package com.biarj.food_ordering_app.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.biarj.food_ordering_app.R

class ConfirmNotification : AppCompatActivity() {

    lateinit var okButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_notification)

        okButton = findViewById(R.id.btnOk)

        okButton.setOnClickListener{

            val intent = Intent(this@ConfirmNotification, AfterLoginActivity :: class.java)
            startActivity(intent)
            finish()

        }

    }
}
