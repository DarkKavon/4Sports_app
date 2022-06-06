package com.example.am_lecture

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class ActivityActivity : AppCompatActivity() {


    lateinit var dbhelper: DBHelper
    private lateinit var shared : SharedPreferences
    private var username : String = ""

    fun formatTime2String(currTime : Int) : String {
        if (currTime < 60) {
            if (currTime < 10) {
                return "00:00:0"+currTime
            }
            else {
                return "00:00:"+currTime
            }
        }
        else if (currTime < 3600) {
            val mins = currTime/60
            val secs = currTime%60
            if (mins < 10) {
                if (secs < 10) {
                    return "00:0"+mins+":0"+secs
                }
                else {
                    return "00:0" + mins + ":" + secs
                }
            }
            else {
                if (secs < 10) {
                    return "00:"+mins+":0"+secs
                }
                else {
                    return "00:" + mins + ":" + secs
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
                        return "0"+hours+":0"+mins+":0"+secs
                    }
                    else {
                        return "0"+hours+":0"+mins+":"+secs
                    }
                }
                else {
                    if (secs < 10) {
                        return "0"+hours+":"+mins+":0"+secs
                    }
                    else {
                        return "0"+hours+":"+mins+":"+secs
                    }
                }
            }
            else {
                if (mins < 10) {
                    if (secs < 10) {
                        return ""+hours+":0"+mins+":0"+secs
                    }
                    else {
                        return ""+hours+":0"+mins+":"+secs
                    }
                }
                else {
                    if (secs < 10) {
                        return ""+hours+":"+mins+":0"+secs
                    }
                    else {
                        return ""+hours+":"+mins+":"+secs
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity)
        shared = getSharedPreferences("com.example.fragmentapp.shared",0)
        getUser()
        val logout_btn = findViewById(R.id.logout_btn) as Button
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
                supportFragmentManager.beginTransaction().replace(R.id.stopwatch_container, stopwatchFragment).commit()

        }
    }
}