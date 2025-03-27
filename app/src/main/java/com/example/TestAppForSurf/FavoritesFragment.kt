package com.example.TestAppForSurf

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.TestAppForSurf.databinding.FragmentFavoritesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteBooksAdapter: BooksAdapter

    lateinit var bookDao: BookDao

    private lateinit var emptyTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root

        val recyclerView: RecyclerView = binding.favoritesRecyclerView
        emptyTextView = binding.emptyTextView // Initialize emptyTextView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize adapter with bookDao
        favoriteBooksAdapter = BooksAdapter(mutableListOf(), bookDao)
        recyclerView.adapter = favoriteBooksAdapter

        loadFavoriteBooks()

        return view
    }

    private fun loadFavoriteBooks() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val favoriteBooks = withContext(Dispatchers.IO) {
                    bookDao.getFavoriteBooks()
                }

                if (favoriteBooks.isEmpty()) {
                    Log.d("FavoritesFragment", "No favorite books found")
                    emptyTextView.visibility = View.VISIBLE
                } else {
                    emptyTextView.visibility = View.GONE
                    val volumes = convertBooksToVolumes(favoriteBooks)
                    updateBooks(volumes)
                }
            } catch (e: Exception) {
                Log.e("FavoritesFragment", "Error loading favorite books", e)
                e.printStackTrace()
                // Consider showing an error message on the UI
            }
        }
    }

    private fun updateBooks(volumes: List<Volume>) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            favoriteBooksAdapter.updateData(volumes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
}