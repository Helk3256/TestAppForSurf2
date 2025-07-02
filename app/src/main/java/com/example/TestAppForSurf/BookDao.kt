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
    fun getAllBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM Book WHERE id = :bookId")
    suspend fun getBookById(bookId: String): Book? // Suspend function

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM Book WHERE isFavorite = 1")
    fun getFavoriteBooksLiveData(): LiveData<List<Book>> // LiveData for favorite books

    @Query("SELECT * FROM Book WHERE isFavorite = 1")
    suspend fun getFavoriteBooks(): List<Book>
}