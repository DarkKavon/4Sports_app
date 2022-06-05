package com.example.am_lecture

import android.content.Context
import android.content.SharedPreferences
import android.widget.ArrayAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.fragment.app.Fragment
import java.lang.ClassCastException

class ListFragment : Fragment() {
    var itemsAdapter: ArrayAdapter<String>? = null
    lateinit var shared : SharedPreferences
    private var posId : Int = 0

    fun setPosition() {
        val edit = shared.edit()
        edit.putInt("posId", posId)
        edit.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemsAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, Route.routeNames) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the xml file for the fragment
        return inflater.inflate(R.layout.fragment_list, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        shared = requireActivity().getSharedPreferences("com.example.fragmentapp.shared",0)
        val lvItems = view.findViewById(R.id.lvItems) as ListView
        lvItems.adapter = itemsAdapter
        lvItems.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            // go to activity to load pizza details fragment
            posId = position
            setPosition()
            listener!!.onItemSelected(position) // (3) Communicate with Activity using Listener
        }
    }

    private var listener: OnItemSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnItemSelectedListener) {      // context instanceof YourActivity
            listener = context // = (YourActivity) context
        } else {
//            ???????????????????
        }
    }

    interface OnItemSelectedListener {
        fun onItemSelected(position: Int)
    }
}