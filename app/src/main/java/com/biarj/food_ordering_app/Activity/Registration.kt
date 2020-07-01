package com.biarj.food_ordering_app.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.biarj.food_ordering_app.R
import org.json.JSONException
import org.json.JSONObject

class Registration : AppCompatActivity() {

    lateinit var etRegistrationName: EditText
    lateinit var etRegistrationEmail :EditText
    lateinit var etRegistrationNumber : EditText
    lateinit var etRegistrationAddress : EditText
    lateinit var etRegistrationPassword : EditText
    lateinit var etRegistrationConfirmPassword : EditText
    lateinit var btnRegister : Button
    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)


        setContentView(R.layout.activity_registration)

        etRegistrationName = findViewById(R.id.etRegistrationName)
        etRegistrationEmail = findViewById(R.id.etRegistrationEmail)
        etRegistrationNumber = findViewById(R.id.etRegistrationNumber)
        etRegistrationAddress = findViewById(R.id.etRegistrationAddress)
        etRegistrationPassword = findViewById(R.id.etRegistrationPassword)
        etRegistrationConfirmPassword = findViewById(R.id.etRegistrationConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        val queue = Volley.newRequestQueue(this@Registration)
        val url = "http://13.235.250.119/v2/register/fetch_result"

        btnRegister.setOnClickListener {

            val name = etRegistrationName.text.toString()
            val email = etRegistrationEmail.text.toString()
            val mobile_number = etRegistrationNumber.text.toString()
            val password = etRegistrationPassword.text.toString()
            val confirmPassword = etRegistrationConfirmPassword.text.toString()
            val address = etRegistrationAddress.text.toString()

            val jsonParams = JSONObject()
            jsonParams.put("name", name)
            jsonParams.put("mobile_number", mobile_number)
            jsonParams.put("password", password)
            jsonParams.put("email", email)
            jsonParams.put("address", address)

            if(password.length >= 4) {

                if (confirmPassword == password) {

                    val jsonObjectRequest = object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                            try {
                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")

                                if (success) {
                                    val details = data.getJSONObject("data")

                                    sharedPreferences.edit()
                                        .putString("user_id", details.getString("user_id")).apply()
                                    sharedPreferences.edit()
                                        .putString("name", details.getString("name")).apply()
                                    sharedPreferences.edit()
                                        .putString("email", details.getString("email")).apply()
                                    sharedPreferences.edit()
                                        .putString("mobile_number", details.getString("mobile_number")).apply()
                                    sharedPreferences.edit()
                                        .putString("address", details.getString("address")).apply()

                                    val intent =
                                        Intent(this@Registration, AfterLoginActivity::class.java)

                                    startActivity(intent)

                                } else {
                                    Toast.makeText(
                                        this@Registration,
                                        "Some Error occurred",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this@Registration,
                                    "Some Unexpected Error occurred",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }, Response.ErrorListener {

                            Toast.makeText(this@Registration, "Error occurred", Toast.LENGTH_SHORT)
                                .show()
                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "8cac724ac3760c"
                            return headers
                        }
                    }
                    queue.add(jsonObjectRequest)
                } else {
                    Toast.makeText(this@Registration, "Passwords do not match", Toast.LENGTH_LONG)
                        .show()
                }
            }else{
                Toast.makeText(this@Registration, "Password Should have a minimum length of 4 numbers", Toast.LENGTH_LONG).show()
            }
        }
    }
}
