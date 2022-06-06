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
    private var startPos : Int = 0
    private var timeToSave : Int = 0
    private var posId : Int = 0
    private  var stopwatchWork : Int = 0
    private lateinit var stopwatchTextView : TextView
    private lateinit var stopwatchStart : Button
    private lateinit var stopwatchStop : Button
    private lateinit var  stopwatchSave : Button
    private lateinit var shared : SharedPreferences
    private lateinit var dbhelper : DBHelper

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

    private fun getPosition() {
        posId = shared.getInt("posId", 0)
    }

    fun setInitTime() {
        val edit = shared.edit()
        edit.putInt("initTime", currTime)
        edit.apply()
    }

    private fun getInitTime() {
        initTime = shared.getInt("initTime", 0)
    }

    private fun setStartPos() {
        val edit = shared.edit()
        edit.putInt("startPos", startPos)
        edit.apply()
    }

    private fun getStartPos() {
        startPos = shared.getInt("startPos", -1)
    }

    private fun setStopwatchWork() {
        val edit = shared.edit()
        edit.putInt("stwWork", stopwatchWork)
        edit.apply()
    }

    private fun getStopwatchWork() {
        stopwatchWork = shared.getInt("stwWork", 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stopwatch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbhelper = activity?.let { DBHelper(it) }!!
        shared = requireActivity().getSharedPreferences("com.example.fragmentapp.shared",0)
        getStopwatchWork()
        getPosition()
        getStartPos()
        stopwatchTextView = requireView().findViewById(R.id.stopwatch) as TextView
        stopwatchStart = requireView().findViewById(R.id.stopwatch_start) as Button
        stopwatchStop = requireView().findViewById(R.id.stopwatch_stop) as Button
        stopwatchSave = requireView().findViewById(R.id.stopwatch_save) as Button
        stopwatchStop.isEnabled = false
        stopwatchSave.isEnabled = false
        stopwatchStart.isEnabled = true
        println("$stopwatchWork $startPos $posId")
        if (startPos == -1) {
            stopwatchStart.isEnabled = true
            stopwatchSave.isEnabled = false
            stopwatchStop.isEnabled = false
            formatTime(0)
        }
        else if (stopwatchWork == 1 && startPos == posId) {
            getInitTime()
            stoper.start()
            stopwatchStart.isEnabled = false
            stopwatchSave.isEnabled = false
            stopwatchStop.isEnabled = true
        }
        else if (stopwatchWork == 1 && startPos != posId) {
            stopwatchStart.isEnabled = false
            stopwatchSave.isEnabled = false
            stopwatchStop.isEnabled = false
            formatTime(0)
        }
        else if (stopwatchWork == 0 && startPos == posId) {
            stopwatchStart.isEnabled = true
            stopwatchSave.isEnabled = true
            stopwatchStop.isEnabled = false
            getT2S()
            formatTime(timeToSave)
            initTime = 0
            setInitTime()
        }
        else if (stopwatchWork == 0 && startPos != posId){
            stopwatchStart.isEnabled = true
            stopwatchSave.isEnabled = false
            stopwatchStop.isEnabled = false
            initTime = 0
            setInitTime()
            formatTime(0)
        }
        else {
            stopwatchStart.isEnabled = true
            stopwatchSave.isEnabled = false
            stopwatchStop.isEnabled = false
            formatTime(0)
        }

        stopwatchStart.setOnClickListener {
            getInitTime()
            getPosition()
            startPos = posId
            setStartPos()
            stoper.start()
            stopwatchStart.isEnabled = false
            stopwatchSave.isEnabled = false
            stopwatchStop.isEnabled = true
            stopwatchWork = 1
            setStopwatchWork()
        }
        stopwatchStop.setOnClickListener {
            stoper.cancel()
            stopwatchStart.isEnabled = true
            stopwatchSave.isEnabled = true
            stopwatchStop.isEnabled = false
            timeToSave = currTime
            currTime = 0
            stopwatchWork = 0
            setStopwatchWork()
            setT2S()
            setInitTime()
        }
        stopwatchSave.setOnClickListener {
            dbhelper.addResult(posId,timeToSave)
        }
    }

}