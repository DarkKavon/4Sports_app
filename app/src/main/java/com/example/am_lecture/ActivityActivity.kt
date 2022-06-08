package com.example.am_lecture

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
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
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.round

class ActivityActivity : AppCompatActivity(), OnMapReadyCallback {

    private var weather_api_key = "d179ae0e16fe4cceb2130cb51520c687"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var dbhelper: DBHelper
    private lateinit var shared: SharedPreferences
    private var username: String = ""
    private var userId : Int = 0
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
    private lateinit var oldLoc : LatLng
    private lateinit var newLoc :LatLng

    private var recordActivity : Int = 0
    private var sumDistance : Double = 0.0

    private var milisFuture : Long = 10000000
    private var initTime : Int = 0
    private var currTime : Int = 0
    private var timeToSave : Int = 0
    private lateinit var stopwatchTextView : TextView
    private lateinit var stopwatchStart : Button
    private lateinit var stopwatchStop : Button
    private lateinit var  stopwatchSave : Button
    private lateinit var  stopwatchReset : Button
    private lateinit var distanceTextView : TextView


    private val stoper : CountDownTimer = object: CountDownTimer(milisFuture,1000){
        override fun onFinish() {
        }
        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            currTime = ((milisFuture-millisUntilFinished)/1000).toInt()
            if (initTime != 0) {
                currTime += initTime
            }
            if (currTime < 60) {
                if (currTime < 10) {
                    stopwatchTextView.text = "00:00:0$currTime"
                }
                else {
                    stopwatchTextView.text = "00:00:$currTime"
                }
            }
            else if (currTime < 3600) {
                val mins = currTime/60
                val secs = currTime%60
                if (mins < 10) {
                    if (secs < 10) {
                        stopwatchTextView.text = "00:0$mins:0$secs"
                    }
                    else {
                        stopwatchTextView.text = "00:0$mins:$secs"
                    }
                }
                else {
                    if (secs < 10) {
                        stopwatchTextView.text = "00:$mins:0$secs"
                    }
                    else {
                        stopwatchTextView.text = "00:$mins:$secs"
                    }
                }
            }
            else {
                val hours = currTime/3600
                val mins = currTime/60%60
                val secs = currTime%3600%60
                if (hours < 10) {
                    if (mins < 10) {
                        if (secs < 10) {
                            stopwatchTextView.text = "0$hours:0$mins:0$secs"
                        }
                        else {
                            stopwatchTextView.text = "0$hours:0$mins:$secs"
                        }
                    }
                    else {
                        if (secs < 10) {
                            stopwatchTextView.text = "0$hours:$mins:0$secs"
                        }
                        else {
                            stopwatchTextView.text = "0$hours:$mins:$secs"
                        }
                    }
                }
                else {
                    if (mins < 10) {
                        if (secs < 10) {
                            stopwatchTextView.text = "$hours:0$mins:0$secs"
                        }
                        else {
                            stopwatchTextView.text = "$hours:0$mins:$secs"
                        }
                    }
                    else {
                        if (secs < 10) {
                            stopwatchTextView.text = "$hours:$mins:0$secs"
                        }
                        else {
                            stopwatchTextView.text = "$hours:$mins:$secs"
                        }
                    }
                }
            }
            setInitTime()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun formatTime(currTime : Int) {
        if (currTime < 60) {
            if (currTime < 10) {
                stopwatchTextView.text = "00:00:0$currTime"
            }
            else {
                stopwatchTextView.text = "00:00:$currTime"
            }
        }
        else if (currTime < 3600) {
            val mins = currTime/60
            val secs = currTime%60
            if (mins < 10) {
                if (secs < 10) {
                    stopwatchTextView.text = "00:0$mins:0$secs"
                }
                else {
                    stopwatchTextView.text = "00:0$mins:$secs"
                }
            }
            else {
                if (secs < 10) {
                    stopwatchTextView.text = "00:$mins:0$secs"
                }
                else {
                    stopwatchTextView.text = "00:$mins:$secs"
                }
            }
        }
        else {
            val hours = currTime/3600
            val mins = currTime/60%60
            val secs = currTime%3600%60
            if (hours < 10) {
                if (mins < 10) {
                    if (secs < 10) {
                        stopwatchTextView.text = "0$hours:0$mins:0$secs"
                    }
                    else {
                        stopwatchTextView.text = "0$hours:0$mins:$secs"
                    }
                }
                else {
                    if (secs < 10) {
                        stopwatchTextView.text = "0$hours:$mins:0$secs"
                    }
                    else {
                        stopwatchTextView.text = "0$hours:$mins:$secs"
                    }
                }
            }
            else {
                if (mins < 10) {
                    if (secs < 10) {
                        stopwatchTextView.text = "$hours:0$mins:0$secs"
                    }
                    else {
                        stopwatchTextView.text = "$hours:0$mins:$secs"
                    }
                }
                else {
                    if (secs < 10) {
                        stopwatchTextView.text = "$hours:$mins:0$secs"
                    }
                    else {
                        stopwatchTextView.text = "$hours:$mins:$secs"
                    }
                }
            }
        }
    }

    private fun setT2S() {
        val edit = shared.edit()
        edit.putInt("t2s", timeToSave)
        edit.apply()
    }

    fun setInitTime() {
        val edit = shared.edit()
        edit.putInt("initTime", currTime)
        edit.apply()
    }

    private fun getInitTime() {
        initTime = shared.getInt("initTime", 0)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity)
        dbhelper = DBHelper(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        shared = getSharedPreferences("com.example.fragmentapp.shared", 0)
        getUser()
        getLocation()



        mapFragment =
            (supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment)!!
        mapFragment.getMapAsync(this)

        val logout_btn = findViewById<Button>(R.id.logout_btn)
        weather_text = findViewById(R.id.weather)
        logout_btn.setOnClickListener {
            username = ""
            setUser()
            Toast.makeText(applicationContext, "Logout successful!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        stopwatchTextView = findViewById(R.id.stopwatch) as TextView
        stopwatchStart = findViewById(R.id.stopwatch_start) as Button
        stopwatchStop = findViewById(R.id.stopwatch_stop) as Button
        stopwatchSave = findViewById(R.id.stopwatch_save) as Button
        stopwatchReset = findViewById(R.id.stopwatch_reset) as Button
        distanceTextView = findViewById(R.id.distnace) as TextView
        stopwatchStop.isEnabled = false
        stopwatchSave.isEnabled = false
        stopwatchStart.isEnabled = true
        stopwatchReset.isEnabled = false

        stopwatchStart.setOnClickListener {
            recordActivity = 1
            getInitTime()
            stoper.start()
            stopwatchStart.isEnabled = false
            stopwatchSave.isEnabled = false
            stopwatchStop.isEnabled = true
            stopwatchReset.isEnabled = false
        }
        stopwatchStop.setOnClickListener {
            recordActivity = 0
            stoper.cancel()
            stopwatchStart.isEnabled = true
            stopwatchSave.isEnabled = true
            stopwatchStop.isEnabled = false
            stopwatchReset.isEnabled = true
            timeToSave = currTime
            setT2S()
            setInitTime()
        }
        stopwatchReset.setOnClickListener() {
            mMap.clear()
            currTime = 0
            timeToSave = 0
            setT2S()
            setInitTime()
            formatTime(0)
            sumDistance = 0.0
            distanceTextView.text = "0km"
            stopwatchReset.isEnabled = false
        }
        stopwatchSave.setOnClickListener {
            dbhelper.addResult(timeToSave,sumDistance,userId)
            sumDistance = 0.0
            mMap.clear()
            currTime = 0
            timeToSave = 0
            distanceTextView.text = "0km"
            setT2S()
            setInitTime()
            formatTime(0)
            stopwatchReset.isEnabled = false
            stopwatchSave.isEnabled = false
        }
    }

    private fun setUser() {
        val edit = shared.edit()
        edit.putString("username", username)
        edit.apply()
    }

    private fun getUser() {
        username = shared.getString("username", "").toString()
        userId = shared.getInt("userId",0)
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
            1000L,
            0f,
            locationListener
        )
    }

    @SuppressLint("SetTextI18n")
    private val locationListener =
        android.location.LocationListener { location -> //handle location change
            println(location)
            latt = location.latitude
            long = location.longitude
            updateMap(mMap)
            getTemp()
            if (temp != "" && city != "") {
                weather_text.text = "Now it's $temp°C in $city"
            }
        }

    private fun getTemp() {
        val queue = Volley.newRequestQueue(this)
        val url =
            "https://api.weatherbit.io/v2.0/current?lat=$latt&lon=$long&key=$weather_api_key"
        println("lat $url")

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                val obj = JSONObject(response)
                val arr = obj.getJSONArray("data")
                val obj2 = arr.getJSONObject(0)
//                println(obj2.getString("temp") + " deg Celsius in " + obj2.getString("city_name"))
                temp = obj2.getString("temp")
                city = obj2.getString("city_name")
            },
            { println("That didn't work!") })
        queue.add(stringReq)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        oldLoc = LatLng(latt, long)
        currMarkerOptions = MarkerOptions().position(oldLoc).title("Marker in $city")
        currMarker = p0.addMarker(currMarkerOptions)!!
        p0.moveCamera(CameraUpdateFactory.newLatLngZoom(oldLoc, zoomLevel))
    }

    private fun updateMap(p0: GoogleMap) {
        newLoc = LatLng(latt, long)
        currMarker.remove()
        currMarkerOptions = MarkerOptions().position(newLoc).title("Marker in $city")
        currMarker = p0.addMarker(currMarkerOptions)!!
        p0.moveCamera(CameraUpdateFactory.newLatLngZoom(newLoc, zoomLevel))

        //DRAW HERE
        if (oldLoc != LatLng(0.0,0.0)) {
            if (recordActivity == 1) {
                drawPrimaryLinePath()
                sumDistance += calcDistance(oldLoc,newLoc)
                val df = DecimalFormat("#.##")
                df.roundingMode = RoundingMode.DOWN
                val roundoff = df.format(sumDistance)
                distanceTextView.text = roundoff.toString() + "km"
            }
        }

        oldLoc = newLoc
    }

    private fun drawPrimaryLinePath() {
        val options = PolylineOptions()
        options.color(Color.parseColor("#CC0000FF"))
        options.width(5f)
        options.visible(true)
        options.add(oldLoc).add(newLoc)
        mMap.addPolyline(options)
    }

    private fun calcDistance(loc1 :LatLng, loc2 :LatLng): Double {
        var lat1 = loc1.latitude
        var lon1 = loc1.longitude
        var lat2 = loc2.latitude
        var lon2 = loc2.longitude
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }




}




