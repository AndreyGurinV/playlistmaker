package com.example.playlistmaker

class Track (
    var trackId: Int,// уникальный идентифкатор
    var trackName: String, // Название композиции
    var artistName: String, // Имя исполнителя
    var trackTimeMillis: Int,
    var artworkUrl100: String, // Ссылка на изображение обложки
)
