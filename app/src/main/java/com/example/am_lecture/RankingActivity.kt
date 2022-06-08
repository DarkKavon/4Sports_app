package com.example.am_lecture

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RankingActivity : AppCompatActivity() {

    private lateinit var shared: SharedPreferences
    private var username: String = ""
    private var userId : Int = 0
    private lateinit var back_btn : Button

    private fun getUser() {
        username = shared.getString("username", "").toString()
        userId = shared.getInt("userId",0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        val dbhelper = DBHelper(this)
        shared = getSharedPreferences("com.example.fragmentapp.shared", 0)
        getUser()

        back_btn = findViewById(R.id.back_btn) as Button

        back_btn.setOnClickListener() {
            val intent = Intent(this, ActivityActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        val messageAdapter = ActivityAdapter(mutableListOf())

        val messageList = findViewById<RecyclerView>(R.id.rankinglist)
        messageList.adapter = messageAdapter
        messageList.layoutManager = LinearLayoutManager(this)


        for(a in dbhelper.selectUserActivities(userId)) {
            val new = Activity(a.id,a.userId,a.time,a.distance,a.date)
            messageAdapter.addMessage(new)
        }
    }

}