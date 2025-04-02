package com.example.playlistmaker.media.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.media.data.dto.PlaylistDto

class PlaylistsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val playlistImageItem: ImageView = itemView.findViewById(R.id.ivPlaylistImageItem)
    private val title: TextView = itemView.findViewById(R.id.tvPlaylistTitleItem)
    private val countTracks: TextView = itemView.findViewById(R.id.tvCountTracksItem)

    fun bind(playlistDto: PlaylistDto) {
        Glide.with(playlistImageItem)
            .load(playlistDto.pathToImage)
            .placeholder(R.drawable.default_album_icon)
            .fitCenter()
            .transform(RoundedCorners(2))
            .into(playlistImageItem)
        title.text = playlistDto.playlistTitle
        countTracks.text = "${playlistDto.tracksCount} треков"
    }
}