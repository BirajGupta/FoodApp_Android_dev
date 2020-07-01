package com.biarj.food_ordering_app.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.biarj.food_ordering_app.R
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    lateinit var profileName : TextView
    lateinit var profileNumber : TextView
    lateinit var profileEmail : TextView
    lateinit var profileAddress : TextView
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences = this.getActivity()!!.getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileName = view.findViewById(R.id.txtProfileName)
        profileNumber = view.findViewById(R.id.txtProfileNumber)
        profileEmail = view.findViewById(R.id.txtProfileEmail)
        profileAddress = view.findViewById(R.id.txtProfileAddress)


        profileName.text = sharedPreferences.getString("name","name")
        profileNumber.text = sharedPreferences.getString("email","email")
        profileEmail.text = sharedPreferences.getString("mobile_number","mobile_number")
        profileAddress.text = sharedPreferences.getString("address","address")


        return view
    }

}
