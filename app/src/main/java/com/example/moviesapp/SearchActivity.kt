package com.example.moviesapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var adapter: MovieAdapter
    private var allMovies: List<Movie> = emptyList() // Store the full list for filtering

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        adapter = MovieAdapter(emptyList()) { movieId, rating, studio ->
            val intent = Intent(this, SearchDetailsActivity::class.java)
            intent.putExtra("MOVIE_ID", movieId)
            intent.putExtra("MOVIE_RATING", rating)
            intent.putExtra("MOVIE_STUDIO", studio)
            startActivity(intent)
        }


        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.moviesRecyclerView.adapter = adapter

        viewModel.movies.observe(this) { movies ->
            allMovies = movies // Store the full list
            adapter.updateMovies(movies)
        }

        viewModel.fetchMovies()

        binding.searchNamedButton.setOnClickListener {
            val query = binding.searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                // Filter movies based on the query
                val filteredMovies = allMovies.filter { movie ->
                    movie.title.contains(query, ignoreCase = true)
                }
                adapter.updateMovies(filteredMovies)

                // Navigate to SearchDetailsActivity with the first matching movie (if any)
                if (filteredMovies.isNotEmpty()) {
                    val firstMatchingMovie = filteredMovies[0]
                    val intent = Intent(this, SearchDetailsActivity::class.java)
                    intent.putExtra("MOVIE_ID", firstMatchingMovie.id)
                    startActivity(intent)
                }
            } else {
                // If query is empty, show all movies
                adapter.updateMovies(allMovies)
            }
        }

        binding.searchButton.setOnClickListener {
            // Already handled by searchNamedButton, but keep for consistency with wireframe
        }

        binding.favoritesButton.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }
}