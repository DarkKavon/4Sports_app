package com.example.am_lecture

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var shared : SharedPreferences
    private var username : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
            val firstFragment = ListFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.flContainer, firstFragment)
                    .addToBackStack(null)
                    .commit()
        }
    }
}