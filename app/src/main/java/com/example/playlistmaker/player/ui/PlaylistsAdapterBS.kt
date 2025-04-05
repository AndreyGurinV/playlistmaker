package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.media.data.dto.PlaylistDto

class PlaylistsAdapterBS(
    private val items: List<PlaylistDto>,
    private val onItemClick: (playlist: PlaylistDto) -> Unit
): RecyclerView.Adapter<PlaylistsViewHolderBS> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolderBS {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item_bs, parent, false)
        return PlaylistsViewHolderBS(view)
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolderBS, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener{
            onItemClick(items[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}