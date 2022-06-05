package com.example.am_lecture

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


class DetailFragment : Fragment() {
    var position = 0
    var tvTitle: TextView? = null
    var tvDetails: TextView? = null
    var bestTime : TextView? = null
    var lastTime : TextView? = null
    lateinit var dbhelper: DBHelper

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
        if (savedInstanceState == null) {
            if (arguments != null) {
                position = arguments!!.getInt("position", 0)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, parent, false)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dbhelper = getActivity()?.let { DBHelper(it) }!!
        tvTitle = view.findViewById(R.id.tvTitle) as TextView
        tvDetails = view.findViewById(R.id.tvDetails) as TextView
        bestTime = view.findViewById(R.id.bestTime) as TextView
        lastTime = view.findViewById(R.id.lastTime) as TextView

        tvTitle!!.text = Route.routeNames[position]
        tvDetails!!.text = Route.routeDetails[position]
        bestTime!!.text = "Best time: "+formatTime2String(dbhelper.selectBestTime(position))+" "+dbhelper.selectBestDate(position)
        lastTime!!.text = "Last time: "+formatTime2String(dbhelper.selectLastTime(position))+" "+dbhelper.selectLastDate(position)

        val stopwatchFragment = StopwatchFragment() as Fragment
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.stopwatch_container, stopwatchFragment).commit()
    }

}

