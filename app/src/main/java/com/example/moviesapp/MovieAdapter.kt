package com.example.moviesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.databinding.ItemMovieBinding

class MovieAdapter(
    private var movies: List<Movie>,
    private val onEditClick: (Movie) -> Unit,
    private val onDeleteClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.title
            binding.movieStudio.text = movie.studio
            binding.movieRating.text = "Rating: ${movie.rating}"

            // Load the movie poster using Glide (assuming there's a URL field in Movie data class)
            Glide.with(binding.movieThumbnail.context)
                .load(movie.posterUrl) // Assuming 'posterUrl' exists in Movie data class
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.movieThumbnail)

            binding.editButton.setOnClickListener { onEditClick(movie) }
            binding.deleteButton.setOnClickListener { onDeleteClick(movie) }
        }
    }
}
