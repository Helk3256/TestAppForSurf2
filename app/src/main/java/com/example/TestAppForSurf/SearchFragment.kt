package com.example.TestAppForSurf

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.TestAppForSurf.databinding.FragmentSearchBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchEditText: EditText
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchProgressBar: ProgressBar
    private lateinit var booksAdapter: BooksAdapter
    private lateinit var googleBooksApi: GoogleBooksApi
    private lateinit var bookDao: BookDao


    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        bookDao = (requireActivity().application as MyApplication).database.bookDao()

        searchEditText = binding.searchEditText
        searchRecyclerView = binding.searchRecyclerView
        searchProgressBar = binding.searchProgressBar

        searchRecyclerView.layoutManager = LinearLayoutManager(context)
        booksAdapter = BooksAdapter(mutableListOf<Volume>(), bookDao)
        searchRecyclerView.adapter = booksAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        googleBooksApi = retrofit.create(GoogleBooksApi::class.java)

        searchEditText.doAfterTextChanged { text ->
            val query = text.toString()
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(300)
                if (query.isNotEmpty()) {
                    searchBooks(query)
                } else {
                    booksAdapter.updateData(emptyList())
                }
            }
        }

        return view
    }

    private suspend fun searchBooks(query: String) {
        showLoading(true)
        try {
            val response: Response<BooksResponse> = googleBooksApi.searchBooks(query)

            if (response.isSuccessful) {
                val booksResponse = response.body()
                val bookItems = booksResponse?.items ?: emptyList()
                booksAdapter.updateData(bookItems)
                Log.d("SearchFragment", "Search results: ${bookItems.size}")
            } else {
                val errorMessage = "Error: ${response.code()} ${response.message()}"
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                Log.e("SearchFragment", errorMessage)
            }
        } catch (e: IOException) {
            Toast.makeText(context, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("SearchFragment", "Network error: ${e.message}")
        } catch (e: HttpException) {
            Toast.makeText(context, "HTTP error: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("SearchFragment", "HTTP error: ${e.message}")
        } catch (e: Exception) {
            Toast.makeText(context, "Unexpected error: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("SearchFragment", "Unexpected error: ${e.message}")
        } finally {
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        searchProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchJob?.cancel()
    }
}