package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TracksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val albumImage: ImageView = itemView.findViewById(R.id.albumImage)

    fun bind(track: Track) {
        Glide.with(albumImage)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.default_album_icon)
            .fitCenter()
            .transform(RoundedCorners(2))
            .into(albumImage)
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.getDuration()
    }
}