package com.example.am_lecture

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class StopwatchFragment : Fragment() {
    private var milisFuture : Long = 10000000
    private var initTime : Int = 0
    private var currTime : Int = 0
    private var timeToSave : Int = 0
    private lateinit var stopwatchTextView : TextView
    private lateinit var stopwatchStart : Button
    private lateinit var stopwatchStop : Button
    private lateinit var  stopwatchSave : Button
    private lateinit var  stopwatchReset : Button
    private lateinit var shared : SharedPreferences
    private lateinit var dbhelper : DBHelper
    private var username : String = ""
    private var userId : Int = 0

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

    private fun getT2S() {
        timeToSave = shared.getInt("t2s", 0)
    }


    fun setInitTime() {
        val edit = shared.edit()
        edit.putInt("initTime", currTime)
        edit.apply()
    }

    private fun getInitTime() {
        initTime = shared.getInt("initTime", 0)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stopwatch, container, false)
    }

    private fun getUser() {
        username = shared.getString("username", "").toString()
        userId = shared.getInt("userId",0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbhelper = activity?.let { DBHelper(it) }!!
        shared = requireActivity().getSharedPreferences("com.example.fragmentapp.shared",0)
        getUser()
        stopwatchTextView = requireView().findViewById(R.id.stopwatch) as TextView
        stopwatchStart = requireView().findViewById(R.id.stopwatch_start) as Button
        stopwatchStop = requireView().findViewById(R.id.stopwatch_stop) as Button
        stopwatchSave = requireView().findViewById(R.id.stopwatch_save) as Button
        stopwatchReset = requireView().findViewById(R.id.stopwatch_reset) as Button
        stopwatchStop.isEnabled = false
        stopwatchSave.isEnabled = false
        stopwatchStart.isEnabled = true
        stopwatchReset.isEnabled = false


        stopwatchStart.setOnClickListener {
            getInitTime()
            stoper.start()
            stopwatchStart.isEnabled = false
            stopwatchSave.isEnabled = false
            stopwatchStop.isEnabled = true
            stopwatchReset.isEnabled = false
        }
        stopwatchStop.setOnClickListener {
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
            currTime = 0
            timeToSave = 0
            setT2S()
            setInitTime()
            formatTime(0)
            stopwatchReset.isEnabled = false
        }
        stopwatchSave.setOnClickListener {
            dbhelper.addResult(timeToSave,0,userId)
            stopwatchSave.isEnabled = false
        }
    }

}