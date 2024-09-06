package com.example.locoapp.network

import com.example.locoapp.core.MovieSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDBApiService {
    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") searchTerm: String,
        @Query("page") page: Int
    ): Response<MovieSearchResponse>
}