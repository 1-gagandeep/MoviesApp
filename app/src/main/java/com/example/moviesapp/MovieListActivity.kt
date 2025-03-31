package com.example.moviesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.databinding.ActivityMovieListBinding
import com.google.firebase.auth.FirebaseAuth

class MovieListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieListBinding
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUserAuth()
        setupRecyclerView()
        viewModel.fetchMovies()

        viewModel.movies.observe(this) { movies ->
            adapter.updateMovies(movies) // ✅ Use updateMovies() instead of submitList()
        }

        binding.addMovieButton.setOnClickListener {
            val intent = Intent(this, AddEditMovieActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun checkUserAuth() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = MovieAdapter(
            movies = emptyList(),
            onEditClick = { movie ->
                val intent = Intent(this, AddEditMovieActivity::class.java)
                intent.putExtra("MOVIE_ID", movie.id.toString()) // ✅ Convert to String
                startActivity(intent)
            },
            onDeleteClick = { movie ->
                viewModel.deleteMovie(movie.toString())
                Toast.makeText(this, "Movie deleted", Toast.LENGTH_SHORT).show()
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

}
