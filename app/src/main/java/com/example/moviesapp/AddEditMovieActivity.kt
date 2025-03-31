package com.example.moviesapp

import androidx.appcompat.app.AppCompatActivity
import com.example.moviesapp.databinding.ActivityAddEditMovieBinding

class AddEditMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditMovieBinding
    private val viewModel: MovieViewModel by viewModels()
    private var movieId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieId = intent.getStringExtra("MOVIE_ID")

        if (movieId != null) {
            viewModel.getMovieById(movieId!!).observe(this) { movie ->
                binding.titleEditText.setText(movie?.title)
                binding.studioEditText.setText(movie?.studio)
                binding.ratingEditText.setText(movie?.rating.toString())
                binding.addEditButton.text = "Update Movie"
            }
        } else {
            binding.addEditButton.text = "Add Movie"
        }

        binding.addEditButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val studio = binding.studioEditText.text.toString()
            val rating = binding.ratingEditText.text.toString().toDouble()

            if (title.isNotEmpty() && studio.isNotEmpty()) {
                if (movieId == null) {
                    viewModel.addMovie(title, studio, rating)
                } else {
                    viewModel.updateMovie(movieId!!, title, studio, rating)
                }
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }
    }
}
