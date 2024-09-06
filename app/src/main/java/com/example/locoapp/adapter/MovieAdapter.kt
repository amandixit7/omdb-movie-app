package com.example.locoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.locoapp.core.Movie
import com.example.locoapp.R
import com.example.locoapp.databinding.MovieItemBinding

class MovieAdapter(private var movies: MutableList<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(private val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                movieTitle.text = movie.Title
                movieYear.text = movie.Year
                movieRating.text = "Rating : ${movie.Rating}"
                itemType.text = "(" + movie.Type.capitalizeFirstLetter() + ")"

                val posterUrl = movie.Poster
                if (posterUrl.isNullOrEmpty() || posterUrl == "N/A") {
                    moviePoster.setImageResource(R.drawable.ic_not_found_foreground)
                } else {
                    Glide.with(moviePoster.context).load(movie.Poster).into(moviePoster)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.itemView.alpha = 0f
        holder.itemView.animate()
            .alpha(1f)
            .setDuration(300)
            .start()
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size


    fun updateMovies(newMovies: List<Movie>) {
        val diffCallback = MovieDiffCallback(movies, newMovies)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        movies.clear()
        movies.addAll(newMovies)
        diffResult.dispatchUpdatesTo(this)
    }

    fun String.capitalizeFirstLetter(): String {
        return if (isNotEmpty()) {
            this[0].uppercase() + substring(1)
        } else {
            this
        }
    }
}


