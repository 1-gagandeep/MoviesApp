
package com.example.moviesapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviesapp.databinding.ActivitySearchDetailsBinding

class SearchDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchDetailsBinding
    private lateinit var viewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchDetailsBinding.inflate(layoutInflater)
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
                binding.movieDescription.text = movie.description
                Glide.with(this)
                    .load(movie.posterUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(binding.movieImage)
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.addToFavButton.setOnClickListener {
            viewModel.addToFavorites(movieId)
            Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
        }
    }
}

