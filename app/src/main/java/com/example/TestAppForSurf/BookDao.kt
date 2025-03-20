package com.example.TestAppForSurf

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface BookDao {
    @Query("SELECT * FROM Book")
    fun getAllBooks(): LiveData<List<Book>> // LiveData для автоматического обновления UI

    @Query("SELECT * FROM Book WHERE id = :bookId")
    fun getBookById(bookId: String): Book?

    @Insert(onConflict = OnConflictStrategy.REPLACE) // перезапись при конфликте ID
    suspend fun insertBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM Book WHERE isFavorite = 1")
    suspend fun getFavoriteBooks(): List<Book> // Метод для получения избранных книг
}