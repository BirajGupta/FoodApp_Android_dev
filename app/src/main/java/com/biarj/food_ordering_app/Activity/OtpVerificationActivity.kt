package com.biarj.food_ordering_app.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.biarj.food_ordering_app.R
import kotlinx.android.synthetic.main.activity_otp_verification.*
import org.json.JSONException
import org.json.JSONObject

class OtpVerificationActivity : AppCompatActivity() {

    lateinit var etOtp : EditText
    lateinit var etNewPassword : EditText
    lateinit var etConfirmNewPassword  : EditText
    lateinit var btnSubmit : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

        etOtp = findViewById(R.id.etOtp)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword)
        btnSubmit = findViewById(R.id.btnSubmit)

        val mobileNumber = intent.getStringExtra("mobileNumber")
        println(mobileNumber)

        val queue = Volley.newRequestQueue(this@OtpVerificationActivity)

        val url = "http://13.235.250.119/v2/reset_password/fetch_result"

        val jsonParams = JSONObject()

        btnSubmit.setOnClickListener{

            val otpReceived = etOtp.text.toString()
            val newPassword = etNewPassword.text.toString()
            println(otpReceived)
            println(newPassword)

            if (otpReceived != "" && newPassword != "") {

                jsonParams.put("mobile_number", mobileNumber)
                jsonParams.put("password", newPassword)
                jsonParams.put("otp", otpReceived)

                val jsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                        try {

                            val success = it.getJSONObject("data").getBoolean("success")
                            println(success)

                            if (success) {

                                val successMessage = it.getJSONObject("data").getString("successMessage")

                                Toast.makeText(this@OtpVerificationActivity, successMessage, Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@OtpVerificationActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()

                            } else {

                                Toast.makeText(
                                    this@OtpVerificationActivity, "Some Error Occurred",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }


                        } catch (e: JSONException) {

                            Toast.makeText(
                                this@OtpVerificationActivity,
                                "Some error occurred in response",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }, Response.ErrorListener {

                        Toast.makeText(
                            this@OtpVerificationActivity,
                            "Some error occurred in error listener",
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
                Toast.makeText(this@OtpVerificationActivity, "Enter credentials", Toast.LENGTH_SHORT).show()

            }

        }



    }
}
