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
            .transform(RoundedCorners(8))
            .into(playlistImageItem)
        title.text = playlistDto.playlistTitle
        countTracks.text = formatTracks(playlistDto.tracksCount.toInt())
    }

    private fun formatTracks(count: Int): String {
        val lastTwoDigits = count % 100
        val lastDigit = count % 10

        return when {
            lastTwoDigits in 11..19 -> "$count треков"
            lastDigit == 1 -> "$count трек"
            lastDigit in 2..4 -> "$count трека"
            else -> "$count треков"
        }
    }

}