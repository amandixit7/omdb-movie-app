package com.example.locoapp.ui

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locoapp.adapter.MovieAdapter
import com.example.locoapp.viewmodel.MovieViewModel
import com.example.locoapp.R
import com.example.locoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieAdapter: MovieAdapter

    private val viewModel: MovieViewModel by viewModels()
    private var grid = false
    private var searchTerm = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnSearch.setOnClickListener {
            searchTerm = binding.etSearchTerm.text.toString()
            if (searchTerm.isEmpty()) {
                Toast.makeText(this, "Search Term cannot be empty", Toast.LENGTH_LONG).show()
            } else {
                performSearch()
                hideKeyboard()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miToggle -> toggleLayout()

            R.id.miSortByYear -> sortByYear()

            R.id.miSortByRating -> sortByRating()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun performSearch() {
        movieAdapter = MovieAdapter(mutableListOf())

        binding.recyclerView.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && dy > 0) {
                        viewModel.loadNextPage(searchTerm)
                    }
                }
            })
        }

        viewModel.movies.observe(this) {
            movieAdapter.updateMovies(it)
        }

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                // Clear error message after displaying
                viewModel.clearError()
            }
        }

        viewModel.resetSearch()
        viewModel.searchMovies(searchTerm)
    }

    private fun toggleLayout() {
        val scrollPosition =
            (binding.recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                ?: 0

        grid = !grid
        binding.recyclerView.layoutManager = if (grid) {
            GridLayoutManager(this, 2)
        } else {
            LinearLayoutManager(this)
        }
        binding.recyclerView.adapter = movieAdapter // Re-set the adapter after layout change

        // Restore the scroll position
        (binding.recyclerView.layoutManager as? LinearLayoutManager)?.scrollToPosition(
            scrollPosition
        )

        // Trigger layout animation after layout change
        TransitionManager.beginDelayedTransition(
            binding.recyclerView,
            TransitionInflater.from(this).inflateTransition(R.transition.transition_fade)
        )

    }


    private fun sortByYear() {
        viewModel.sortMoviesByYear()
        binding.recyclerView.scrollToPosition(0)
    }

    private fun sortByRating() {
        viewModel.sortMoviesByRating()
        binding.recyclerView.scrollToPosition(0)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = currentFocus
        currentFocusView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        } ?: run {
            // If there is no currently focused view, hide the keyboard from the window
            inputMethodManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }
    }
}


