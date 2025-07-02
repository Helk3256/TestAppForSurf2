package com.example.TestAppForSurf

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val bookDao = BookDatabase.getDatabase(application).bookDao()
    val favoriteBooksLiveData: LiveData<List<Book>> = bookDao.getFavoriteBooksLiveData()



    fun toggleFavorite(volume: Volume) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = convertVolumeToBook(volume)
            bookDao.insertBook(book) // This will update LiveData
        }
    }

    private suspend fun convertVolumeToBook(volume: Volume): Book {
        val existingBook = bookDao.getBookById(volume.id)

        return if (existingBook != null) {
            // Update existing book
            val updatedBook = existingBook.copy(isFavorite = !existingBook.isFavorite)
            return updatedBook
        } else {
            // Create new book
            Book(
                id = volume.id,
                title = volume.volumeInfo?.title ?: "No Title",
                authors = volume.volumeInfo?.authors.toString(),
                description = volume.volumeInfo?.description ?: "No Description",
                publishedDate = volume.volumeInfo?.publishedDate ?: "Unknown",
                imageLinks = volume.volumeInfo?.imageLinks?.thumbnail ?: "",
                isFavorite = true // Assuming that toggling means it's becoming a favorite
            )
        }
    }
}