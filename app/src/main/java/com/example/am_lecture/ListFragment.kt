package com.example.am_lecture

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


class ListFragment : Fragment() {
    private lateinit var lvItems: ListView
    lateinit var shared : SharedPreferences
    private var posId : Int = 0
    private var listener: AdapterView.OnItemSelectedListener? = null

    fun setPosition() {
        val edit = shared.edit()
        edit.putInt("posId", posId)
        edit.apply()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, parent, false)
    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shared = getActivity()?.getSharedPreferences("com.example.fragmentapp.shared",0)!!
        lvItems = getView()?.findViewById<ListView>(R.id.lvItems) as ListView
        var items = Route.routeNames
        val adapter = ArrayAdapter(requireActivity(),android.R.layout.simple_list_item_1,items)
        lvItems.adapter = adapter
        lvItems.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            var fm : FragmentManager? = getActivity()?.getSupportFragmentManager()
            val bundle = Bundle()
            bundle.putInt("position",position)
            posId = position
            setPosition()
            var secondFragment = DetailFragment()
            secondFragment.arguments = bundle
            fm?.beginTransaction()?.replace(R.id.flContainer, secondFragment)?.addToBackStack(null)?.commit()
        })

    }



}

