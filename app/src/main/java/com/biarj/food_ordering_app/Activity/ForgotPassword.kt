package com.biarj.food_ordering_app.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.biarj.food_ordering_app.R
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONException
import org.json.JSONObject

class ForgotPassword : AppCompatActivity() {

    lateinit var btnNext: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        btnNext = findViewById(R.id.btnNext)


        val queue = Volley.newRequestQueue(this@ForgotPassword)

        val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

        val jsonParams = JSONObject()

        btnNext.setOnClickListener {

            val mobileNumber = etInfoMobileNumber.text.toString()
            val email = etInfoEmail.text.toString()

            if (email != "" && mobileNumber != "") {

                jsonParams.put("mobile_number", mobileNumber)
                jsonParams.put("email", email)

                val jsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                        try {

                            val success = it.getJSONObject("data").getBoolean("success")

                            if (success) {


                                val intent =
                                    Intent(this@ForgotPassword, OtpVerificationActivity::class.java)
                                intent.putExtra("mobileNumber", mobileNumber)
                                startActivity(intent)
                                finish()

                            } else {

                                Toast.makeText(
                                    this@ForgotPassword, "Some Error Occurred",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }


                        } catch (e: JSONException) {

                            print(e)

                            Toast.makeText(
                                this@ForgotPassword,
                                "Some error occurred in response",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }, Response.ErrorListener {

                        Toast.makeText(
                            this@ForgotPassword,
                            "Some error occurred in errror listener",
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
            } else {
                Toast.makeText(this@ForgotPassword, "Enter credentials", Toast.LENGTH_SHORT).show()

            }

        }
    }
}
