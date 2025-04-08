package com.aslmmovic.bosta_task.presenation.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aslmmovic.bosta_task.data.model.DistrictWithCity

class CitiesAdapter(private val districtsWithCity: List<DistrictWithCity>) :
    RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(android.R.id.text1)
        val districtName: TextView = itemView.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cityName.text = districtsWithCity[position].cityName
        holder.districtName.text = districtsWithCity[position].district.districtName
    }

    override fun getItemCount() = districtsWithCity.size
}