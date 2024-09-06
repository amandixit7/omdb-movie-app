package com.example.locoapp.core

data class MovieSearchResponse(
    val Search: List<Movie>,
    val totalResults: String,
    val Response: String
)

data class Movie(
    val Title: String,
    val Year: String,
    val imdbID: String,
    val Type: String,
    val Poster: String,
    var Rating: Double = 0.0
)


