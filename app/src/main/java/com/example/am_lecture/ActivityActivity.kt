package com.example.am_lecture

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.util.jar.Manifest

class ActivityActivity : AppCompatActivity() {

    var weather_url = ""
    var api_id = "d179ae0e16fe4cceb2130cb51520c687"
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationManager: LocationManager

    lateinit var dbhelper: DBHelper
    private lateinit var shared : SharedPreferences
    private var username : String = ""

    private var latt : Double = 0.0
    private var long : Double = 0.0
    private var city : String = ""
    private var temp : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        shared = getSharedPreferences("com.example.fragmentapp.shared",0)
        getUser()
        getLocation()
        val logout_btn = findViewById(R.id.logout_btn) as Button
        val update_weather_btn = findViewById(R.id.update_weather) as Button
        val weather_text = findViewById(R.id.weather) as TextView
        logout_btn.setOnClickListener() {
            username = ""
            setUser()
            Toast.makeText(applicationContext, "Logout successful!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        update_weather_btn.setOnClickListener() {
            getTemp()
            if (temp == "" || city == "")
            {
                weather_text.text = "Click update weather"
            }
            else {
                weather_text.text = "Now it's "+temp+"°C in "+city
            }

        }
    }

    private fun setUser() {
        val edit = shared.edit()
        edit.putString("username", username)
        edit.apply()
    }

    private fun getUser() {
        username = shared.getString("username", "").toString()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
                dbhelper = DBHelper(this)
                val stopwatchFragment = StopwatchFragment() as Fragment
                supportFragmentManager.beginTransaction().replace(R.id.stopwatch_container, stopwatchFragment).commit()
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(applicationContext, "Location permission needed!", Toast.LENGTH_SHORT).show()
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 0f, locationListener)
    }

    private val locationListener =
        android.location.LocationListener { location -> //handle location change
            println(location)
            latt = location.latitude
            long = location.longitude
        }

    fun getTemp() {
        val queue = Volley.newRequestQueue(this)
        val url: String = "https://api.weatherbit.io/v2.0/current?" + "lat=" + latt + "&lon=" + long + "&key=" + api_id
        println("lat " + url)

        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
//                println("lat " + response.toString())

                val obj = JSONObject(response)

                val arr = obj.getJSONArray("data")
//                println("lat obj1 " + arr.toString())

                val obj2 = arr.getJSONObject(0)
//                println("lat obj2 " + obj2.toString())

                println(obj2.getString("temp") + " deg Celsius in " + obj2.getString("city_name"))
                temp = obj2.getString("temp")
                city = obj2.getString("city_name")
            },
            // In case of any error
            Response.ErrorListener { println("That didn't work!") })
        queue.add(stringReq)
    }
}

