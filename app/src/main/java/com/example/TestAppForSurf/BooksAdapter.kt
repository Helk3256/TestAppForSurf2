package com.example.TestAppForSurf

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BooksAdapter(private var books: MutableList<Volume>, private val bookDao: BookDao) :
    RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookCover: ImageView = itemView.findViewById(R.id.bookCover)
        val bookTitle: TextView = itemView.findViewById(R.id.bookTitle)
        val bookAuthors: TextView = itemView.findViewById(R.id.bookAuthors)
        val favoriteButton: ImageButton = itemView.findViewById(R.id.favoriteButton)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        Log.d("Adapter", "Binding position: $position")
        val book = books.getOrNull(position) ?: run {
            Log.e("Adapter", "Book is null at position: $position")
            return // Exit early if book is null
        }
        holder.bookTitle.text = book.volumeInfo?.title ?: "No Title"
        holder.bookAuthors.text = book.volumeInfo?.authors?.joinToString(", ") ?: "No Authors"

        // Загрузка изображения с использованием Glide
        val coverUrl = book.volumeInfo?.imageLinks?.thumbnail ?: ""
        Glide.with(holder.itemView.context)
            .load(coverUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.bookCover)

        holder.favoriteButton.setOnClickListener {
            // Handle favorite button click
            toggleFavorite(book)
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }

    fun updateData(newBooks: List<Volume>) {
        val diffResult = DiffUtil.calculateDiff(BookDiffCallback(books, newBooks))
        books.clear()
        books.addAll(newBooks)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun toggleFavorite(volume: Volume) {
        CoroutineScope(Dispatchers.IO).launch {
            // Convert Volume to Book entity
            val book = convertVolumeToBook(volume)

            // Toggle isFavorite
            book.isFavorite = !book.isFavorite // Assuming Book has isFavorite

            // Update database
            bookDao.insertBook(book)
        }
    }

    private fun convertVolumeToBook(volume: Volume): Book {
        return Book(
            id = volume.id,
            title = volume.volumeInfo?.title ?: "No Title",
            authors = volume.volumeInfo?.authors ?: emptyList(),
            isFavorite = false,
            imageLinks = volume.volumeInfo?.imageLinks , // Используем thumbnail или пустую строку
            publishedDate = volume.volumeInfo?.publishedDate ?: "Unknown", // Используем publishedDate или "Unknown"
            description = volume.volumeInfo?.description ?: "No Description" // Используем description или "No Description"
        )
    }


    class BookDiffCallback(
        private val oldList: List<Volume>,
        private val newList: List<Volume>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id // Assuming 'id' is unique
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition] // Compare data contents
        }
    }
}