package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView

class TracksAdapter(
    private val items: List<Track>,
    private val onItemClick: (track: Track) -> Unit
) : RecyclerView.Adapter<TracksViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener{
            onItemClick(items[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}