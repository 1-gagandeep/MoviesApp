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
    private var allMovies: List<Movie> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        adapter = MovieAdapter(emptyList()) { movieId, rating, studio, posterUrl ->
            val intent = Intent(this, SearchDetailsActivity::class.java)
            intent.putExtra("MOVIE_ID", movieId)
            intent.putExtra("MOVIE_RATING", rating)
            intent.putExtra("MOVIE_STUDIO", studio)
            intent.putExtra("MOVIE_POSTER", posterUrl)
            startActivity(intent)
        }

        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.moviesRecyclerView.adapter = adapter

        viewModel.movies.observe(this) { movies ->
            allMovies = movies
            adapter.updateMovies(movies)
        }

        viewModel.fetchMovies()

        binding.searchNamedButton.setOnClickListener {
            val query = binding.searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                val filteredMovies = allMovies.filter { movie ->
                    movie.title.contains(query, ignoreCase = true)
                }
                adapter.updateMovies(filteredMovies)

                if (filteredMovies.isNotEmpty()) {
                    val firstMatchingMovie = filteredMovies[0]
                    val intent = Intent(this, SearchDetailsActivity::class.java)
                    intent.putExtra("MOVIE_ID", firstMatchingMovie.id)
                    intent.putExtra("MOVIE_RATING", firstMatchingMovie.rating)
                    intent.putExtra("MOVIE_STUDIO", firstMatchingMovie.studio)
                    intent.putExtra("MOVIE_POSTER", firstMatchingMovie.posterUrl)
                    startActivity(intent)
                }
            } else {
                adapter.updateMovies(allMovies)
            }
        }

        binding.favoritesButton.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }
}

