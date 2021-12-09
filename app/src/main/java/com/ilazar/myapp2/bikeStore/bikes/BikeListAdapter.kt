package com.ilazar.myapp2.bikeStore.bikes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ilazar.myapp2.R
import com.ilazar.myapp2.core.TAG
import com.ilazar.myapp2.bikeStore.data.Bike
import com.ilazar.myapp2.bikeStore.bike.BikeEditFragment

class BikeListAdapter(
    private val fragment: Fragment,
) : RecyclerView.Adapter<BikeListAdapter.ViewHolder>() {

    var bikes = emptyList<Bike>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onBikeClick: View.OnClickListener = View.OnClickListener { view ->
        val bike = view.tag as Bike
        fragment.findNavController().navigate(R.id.BikeEditFragment, Bundle().apply {
            putString(BikeEditFragment.BIKE_ID, bike._id)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_bike, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val bike = bikes[position]
        holder.nameView.text = bike.name
//        holder.conditionView.text = bike.condition
//        holder.warrantyView.text = bike.warranty.toString()
        holder.priceView.text = bike.price.toString()
        holder.itemView.tag = bike
        holder.itemView.setOnClickListener(onBikeClick)
    }

    override fun getItemCount() = bikes.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.name)
//        val conditionView: TextView = view.findViewById(R.id.condition)
//        val warrantyView: TextView = view.findViewById(R.id.warranty)
        val priceView: TextView = view.findViewById(R.id.price)

    }
}
