package com.example.am_lecture

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var dbhelper: DBHelper
    private lateinit var shared : SharedPreferences
    private var username : String = ""
    private var userId : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        shared = getSharedPreferences("com.example.fragmentapp.shared",0)
        dbhelper = DBHelper(this)
        val editLogin = findViewById(R.id.editTextUsername) as EditText
        val editPassword = findViewById(R.id.editTextPassword) as EditText
        val loginBtn = findViewById(R.id.login_btn) as Button

        getUser()
        if (username != "") {
            val intent = Intent(this, ActivityActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        loginBtn.setOnClickListener() {
            username = editLogin.text.toString()
            val passwd = editPassword.text.toString()
            if (dbhelper.checkIfUserExists(username) == 1) {
                if (dbhelper.checkPassword(username,passwd) == 1) {
                    editLogin.text.clear()
                    editPassword.text.clear()
                    Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_SHORT).show()
                    userId = dbhelper.selectUserId(username)
                    setUser()
                    val intent = Intent(this, ActivityActivity::class.java)
                    startActivity(intent)
                    this.finish()
                }
                else {
                    Toast.makeText(applicationContext, "WRONG PASSWORD!", Toast.LENGTH_SHORT).show()
                    editPassword.text.clear()
                }
            }
            else {
                dbhelper.addUser(username,passwd)
                Toast.makeText(applicationContext, "Your account is created!", Toast.LENGTH_SHORT).show()
                userId = dbhelper.selectUserId(username)
                setUser()
                editLogin.text.clear()
                editPassword.text.clear()
                val intent = Intent(this, ActivityActivity::class.java)
                startActivity(intent)
                this.finish()
            }
        }
    }

    private fun setUser() {
        val edit = shared.edit()
        edit.putString("username", username)
        edit.putInt("userId", userId)
        edit.apply()
    }

    private fun getUser() {
        username = shared.getString("username", "").toString()
        userId = shared.getInt("userId",0)
    }


}