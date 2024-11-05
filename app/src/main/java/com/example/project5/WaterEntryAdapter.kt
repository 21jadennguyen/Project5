package com.example.project5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WaterEntryAdapter(private val entries: List<WaterEntry>) :
    RecyclerView.Adapter<WaterEntryAdapter.WaterEntryViewHolder>() {

    class WaterEntryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val volumeText: TextView = view.findViewById(R.id.volumeText)
        val dateText: TextView = view.findViewById(R.id.dateText)
        val noteText: TextView = view.findViewById(R.id.noteText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaterEntryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.water_entry_item, parent, false)
        return WaterEntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: WaterEntryViewHolder, position: Int) {
        val entry = entries[position]
        holder.volumeText.text = "${entry.volume} ml"
        holder.dateText.text = entry.date
        holder.noteText.text = entry.note ?: "No notes"
    }

    override fun getItemCount() = entries.size
}
