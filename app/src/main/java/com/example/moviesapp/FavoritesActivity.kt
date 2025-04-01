package com.example.moviesapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        adapter = MovieAdapter(emptyList()) { movieId, rating, studio, posterUrl ->
            val intent = Intent(this, FavoritesDetailsActivity::class.java).apply {
                putExtra("MOVIE_ID", movieId ?: "")
                putExtra("MOVIE_RATING", rating.toFloat())
                putExtra("MOVIE_STUDIO", studio ?: "")
                putExtra("MOVIE_POSTER_URL", posterUrl ?: "")
            }
            startActivity(intent)
        }

        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = this@FavoritesActivity.adapter
        }

        viewModel.favorites.observe(this) { favorites ->
            adapter.updateMovies(favorites)
        }

        viewModel.fetchFavorites()

        binding.searchButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }
}

