package com.example.locoapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locoapp.core.Movie
import com.example.locoapp.network.RetrofitInstance
import com.example.locoapp.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.concurrent.Flow
import kotlin.math.round
import kotlin.random.Random


class MovieViewModel : ViewModel() {
    private val repository = MovieRepository(RetrofitInstance.api)

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private var currentPage = 1
    private var isLastPage = false


//    private val movieIds = mutableSetOf<String>()

    fun searchMovies(searchTerm: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.searchMovies(searchTerm, currentPage) }
                // Handle the result on the main thread
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        val newMovies = result.Search.map { movie ->
                            val randomRating = round(Random.nextDouble(1.0, 10.0) * 10) / 10.0
                            movie.copy(Rating = randomRating)
                        }


                        val updatedList = (_movies.value ?: emptyList()) + newMovies
                        _movies.value = updatedList
                        isLastPage = updatedList.size >= result.totalResults.toInt()
                        _error.value = null  // Clear error if any previous remains
                    } else {
                        _error.value =
                            "Failed to load movies. Please try again."  // error message
                    }
                }
            } catch (e: HttpException) {
                // Handle any exceptions that occur
                _error.value = "An error occurred: ${e.message}"
                Log.e("MovieViewModel", "Error in searchMovies: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun clearError() {
        _error.value = null
    }

    fun loadNextPage(searchTerm: String) {
        if (!isLastPage && !_isLoading.value!!) {
            currentPage++
            searchMovies(searchTerm)
        }
    }

    fun sortMoviesByYear() {
        _movies.value = _movies.value?.sortedBy { it.Year }
    }

    fun sortMoviesByRating() {
        _movies.value = _movies.value?.sortedByDescending { it.Rating }
    }

    fun resetSearch() {
//        movieIds.clear()
        currentPage = 1
        isLastPage = false
        _movies.value = emptyList()
    }
}









