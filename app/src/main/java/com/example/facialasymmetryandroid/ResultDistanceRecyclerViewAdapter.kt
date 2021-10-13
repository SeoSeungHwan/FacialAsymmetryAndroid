package com.example.facialasymmetryandroid


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ResultDistanceRecyclerViewAdapter(private val dataSet: Map<String,String>) :
    RecyclerView.Adapter<ResultDistanceRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val distance_index_tv: TextView
        val distance_percent_tv: TextView


        init {
            distance_index_tv = view.findViewById(R.id.distance_index_tv)
            distance_percent_tv = view.findViewById(R.id.distance_percent_tv)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.distance_item, viewGroup, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.distance_index_tv.text = (position+1).toString()+"번"
        viewHolder.distance_percent_tv.text = dataSet.get((position+1).toString())+"%"
    }
    override fun getItemCount() = dataSet.size



}