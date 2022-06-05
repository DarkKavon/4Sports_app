package com.example.am_lecture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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