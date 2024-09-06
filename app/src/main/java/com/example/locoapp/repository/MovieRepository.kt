package com.example.locoapp.repository

import android.util.Log
import com.example.locoapp.core.Constants
import com.example.locoapp.core.MovieSearchResponse
import com.example.locoapp.network.OMDBApiService

class MovieRepository(private val apiService: OMDBApiService) {
    private val TAG = "MovieRepository"

    suspend fun searchMovies(searchTerm: String, page: Int): MovieSearchResponse? {
        return try {
            val response = apiService.searchMovies(Constants.API_KEY, searchTerm, page)
            if (response.isSuccessful) {
                val body = response.body()
                Log.d(TAG, "API response successful: ${body?.Search?.size} items found")
                body
            } else {
                Log.e(TAG, "API response error: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception occurred: ${e.message}", e)
            null
        }
    }
}






