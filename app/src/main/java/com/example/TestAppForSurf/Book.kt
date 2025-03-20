package com.example.TestAppForSurf

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey
    val id: String, // ID книги в Google Books API
    val title: String?,
    val authors: List<String>?,
    val imageLinks: String, // Вложенный класс для ссылок на изображения
    val publishedDate: String?,
    val description: String?,
    var isFavorite: Boolean = false // Флаг для отслеживания избранного
)

