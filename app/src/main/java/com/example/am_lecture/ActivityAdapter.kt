package com.example.am_lecture

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ActivityAdapter(
    private val messages: MutableList<Activity>
) : RecyclerView.Adapter<ActivityAdapter.messageViewHolder>() {
    class messageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val activityIdView = itemView.findViewById<TextView>(R.id.activity_id)
        val distanceView = itemView.findViewById<TextView>(R.id.distance_rank)
        val timeView = itemView.findViewById<TextView>(R.id.time)
        val dateView = itemView.findViewById<TextView>(R.id.date)

        @SuppressLint("SetTextI18n")
        private fun formatTime(currTime : Int): String {
            if (currTime < 60) {
                if (currTime < 10) {
                    return "00:00:0$currTime"
                }
                else {
                    return "00:00:$currTime"
                }
            }
            else if (currTime < 3600) {
                val mins = currTime/60
                val secs = currTime%60
                if (mins < 10) {
                    if (secs < 10) {
                        return "00:0$mins:0$secs"
                    }
                    else {
                        return "00:0$mins:$secs"
                    }
                }
                else {
                    if (secs < 10) {
                        return "00:$mins:0$secs"
                    }
                    else {
                        return "00:$mins:$secs"
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
                            return "0$hours:0$mins:0$secs"
                        }
                        else {
                            return "0$hours:0$mins:$secs"
                        }
                    }
                    else {
                        if (secs < 10) {
                            return "0$hours:$mins:0$secs"
                        }
                        else {
                            return "0$hours:$mins:$secs"
                        }
                    }
                }
                else {
                    if (mins < 10) {
                        if (secs < 10) {
                            return "$hours:0$mins:0$secs"
                        }
                        else {
                            return "$hours:0$mins:$secs"
                        }
                    }
                    else {
                        if (secs < 10) {
                            return "$hours:$mins:0$secs"
                        }
                        else {
                            return "$hours:$mins:$secs"
                        }
                    }
                }
            }
        }

        fun bind(curMessage: Activity){
            activityIdView.text = curMessage.id.toString()
            distanceView.text = curMessage.distance.toString()+"km"
            timeView.text = formatTime(curMessage.time)
            dateView.text = curMessage.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): messageViewHolder {
        return messageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        )
    }

    fun addMessage(message: Activity) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    override fun onBindViewHolder(holder: messageViewHolder, position: Int) {
        val curMessage = messages[position]
        holder.bind(curMessage)
    }

    override fun getItemCount(): Int {
        return messages.size
    }


}