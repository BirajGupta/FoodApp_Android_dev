package com.biarj.food_ordering_app.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.biarj.food_ordering_app.R
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var btnlogin: Button
    lateinit var etMobileNumber : EditText
    lateinit var etPassword: EditText
    lateinit var txtForgotPassword: TextView
    lateinit var txtSignUp: TextView
    lateinit var coordinatorLayout : CoordinatorLayout

    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name),Context.MODE_PRIVATE)

        setContentView(R.layout.activity_login)

        btnlogin = findViewById(R.id.btnLogin)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtSignUp = findViewById(R.id.txtSignUp)
        etMobileNumber = findViewById(R.id.etMobileNumber)
        etPassword = findViewById(R.id.etPassword)
        coordinatorLayout =findViewById(R.id.loginCoordinatorLayout)


        val queue = Volley.newRequestQueue(this@LoginActivity)

        val url = "http://13.235.250.119/v2/login/fetch_result"

        val jsonParams = JSONObject()


        btnlogin.setOnClickListener{

            val mobileNumber = etMobileNumber.text.toString()
            val password = etPassword.text.toString()

            if(password != "" && mobileNumber != "") {

                jsonParams.put("mobile_number", mobileNumber)
                jsonParams.put("password", password)

                if (password.length >= 4) {

                    val jsonObjectRequest =
                        object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                            val success = it.getJSONObject("data").getBoolean("success")
                            try {

                                val details = it.getJSONObject("data").getJSONObject("data")

                                if (success) {

                                    savePreferences()
                                    sharedPreferences.edit()
                                        .putString("user_id", details.getString("user_id")).apply()
                                    sharedPreferences.edit()
                                        .putString("name", details.getString("name")).apply()
                                    sharedPreferences.edit()
                                        .putString("email", details.getString("email")).apply()
                                    sharedPreferences.edit()
                                        .putString(
                                            "mobile_number",
                                            details.getString("mobile_number")
                                        ).apply()
                                    sharedPreferences.edit()
                                        .putString("address", details.getString("address")).apply()

                                    val intent =
                                        Intent(this@LoginActivity, AfterLoginActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                } else {

                                    val errMessage =
                                        it.getJSONObject("data").getString("errorMessage")

                                    Toast.makeText(
                                        this@LoginActivity, errMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }


                            } catch (e: JSONException) {

                                Toast.makeText(
                                    this@LoginActivity,
                                    "Some error occurred",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }, Response.ErrorListener {

                            Toast.makeText(
                                this@LoginActivity,
                                "Some error occurred here1",
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
                }else{

                    Toast.makeText(this@LoginActivity,"Password should have a minimum length of 4 numbers",Toast.LENGTH_SHORT).show()

                }
            }
            else{
                Toast.makeText(this@LoginActivity, "Enter credentials", Toast.LENGTH_SHORT).show()

            }
        }

        txtForgotPassword.setOnClickListener{

            val intent = Intent(this@LoginActivity, ForgotPassword::class.java)
            startActivity(intent)

        }

        txtSignUp.setOnClickListener{

           val intent = Intent(this@LoginActivity,
               Registration::class.java)

            startActivity(intent)
        }


    }


    fun savePreferences(){
        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
    }



}
