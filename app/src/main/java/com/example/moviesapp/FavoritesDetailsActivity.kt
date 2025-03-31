package com.example.moviesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviesapp.databinding.ActivityFavoritesDetailsBinding

class FavoritesDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesDetailsBinding
    private lateinit var viewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        val movieId = intent.getStringExtra("MOVIE_ID") ?: ""

        if (movieId.isEmpty()) {
            finish() // Close activity if no valid movie ID
            return
        }

        viewModel.getMovieById(movieId).observe(this) { movie ->
            if (movie != null) {
                binding.movieTitle.text = movie.title
                binding.movieStudio.text = "Studio: ${movie.studio}"
                binding.movieRating.text = "Rating: ${movie.rating}"
                binding.movieDescription.setText(movie.description)
                Glide.with(this)
                    .load(movie.posterUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(binding.movieImage)
            }
        }

        binding.updateButton.setOnClickListener {
            val newDescription = binding.movieDescription.text.toString()
            viewModel.updateMovieDescription(movieId, newDescription)
        }

        binding.deleteButton.setOnClickListener {
            viewModel.removeFromFavorites(movieId)
            finish()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}