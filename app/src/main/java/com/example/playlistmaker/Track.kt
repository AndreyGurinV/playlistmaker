package com.example.playlistmaker

import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Date
import java.util.Locale

class Track (
    var trackId: Int,// уникальный идентифкатор
    var trackName: String, // Название композиции
    var artistName: String, // Имя исполнителя
    var trackTimeMillis: Int,
    var artworkUrl100: String, // Ссылка на изображение обложки
    var collectionName: String, // Название альбома
    var releaseDate: String, // Год релиза трека
    var primaryGenreName: String, // Жанр трека
    var country: String, // Страна исполнителя
): Serializable {

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    fun getDuration() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    fun getReleaseYear(): String {
        return (Date.from(ZonedDateTime.parse(releaseDate).toInstant()).year + 1900).toString()
    }

}
