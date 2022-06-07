package com.example.am_lecture

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject

class ActivityActivity : AppCompatActivity(), OnMapReadyCallback {

    private var weather_api_key = "d179ae0e16fe4cceb2130cb51520c687"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    lateinit var dbhelper: DBHelper
    private lateinit var shared: SharedPreferences
    private var username: String = ""
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap
    private var latt: Double = 0.0
    private var long: Double = 0.0
    private var city: String = ""
    private var temp: String = ""
    private lateinit var weather_text: TextView
    private lateinit var currMarkerOptions: MarkerOptions
    private lateinit var currMarker: Marker
    private var zoomLevel: Float = 15.0f //This goes up to 21


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        shared = getSharedPreferences("com.example.fragmentapp.shared", 0)
        getUser()
        getLocation()

        mapFragment =
            (supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment)!!
        mapFragment?.getMapAsync(this)

        val logout_btn = findViewById(R.id.logout_btn) as Button
        weather_text = findViewById(R.id.weather) as TextView
        logout_btn.setOnClickListener() {
            username = ""
            setUser()
            Toast.makeText(applicationContext, "Logout successful!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
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
            supportFragmentManager.beginTransaction()
                .replace(R.id.stopwatch_container, stopwatchFragment).commit()
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
            Toast.makeText(applicationContext, "Location permission needed!", Toast.LENGTH_SHORT)
                .show()
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000L,
            0f,
            locationListener
        )
    }

    private val locationListener =
        android.location.LocationListener { location -> //handle location change
            println(location)
            latt = location.latitude
            long = location.longitude
            updateMap(mMap)
            getTemp()
            if (temp == "" || city == "") {
                weather_text.text = "Click update weather"
            } else {
                weather_text.text = "Now it's " + temp + "°C in " + city
            }
        }

    fun getTemp() {
        val queue = Volley.newRequestQueue(this)
        val url: String =
            "https://api.weatherbit.io/v2.0/current?" + "lat=" + latt + "&lon=" + long + "&key=" + weather_api_key
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

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val pos = LatLng(latt, long)
        currMarkerOptions = MarkerOptions().position(pos).title("Marker in " + city)
        currMarker = p0.addMarker(currMarkerOptions)!!
//        p0.moveCamera(CameraUpdateFactory.newLatLng(pos))
        p0.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoomLevel));
    }

    private fun updateMap(p0: GoogleMap) {
        val pos = LatLng(latt, long)
        currMarker.remove()
        currMarkerOptions = MarkerOptions().position(pos).title("Marker in " + city)
        currMarker = p0.addMarker(currMarkerOptions)!!
//        p0.moveCamera(CameraUpdateFactory.newLatLng(pos))
        p0.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoomLevel));
    }
}




