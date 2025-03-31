//package com.example.moviesapp
//
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.moviesapp.databinding.ItemMovieBinding
//
//class MovieAdapter(
//    private var movies: List<Movie>,
//    private val onEditClick: (Movie) -> Unit,
//    private val onDeleteClick: (String?) -> Unit // Changed to accept String? (movieId)
//) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
//        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MovieViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
//        val movie = movies[position]
//        holder.bind(movie)
//    }
//
//    override fun getItemCount(): Int = movies.size
//
//    fun updateMovies(newMovies: List<Movie>) {
//        movies = newMovies
//        notifyDataSetChanged()
//    }
//
//    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(movie: Movie) {
//            binding.movieTitle.text = movie.title
//            binding.movieStudio.text = movie.studio
//            binding.movieRating.text = "Rating: ${movie.rating}"
//
//            // Load the movie poster using Glide
//            Glide.with(binding.movieThumbnail.context)
//                .load(movie.posterUrl)
//                .placeholder(R.drawable.ic_launcher_background)
//                .into(binding.movieThumbnail)
//
//            binding.editButton.setOnClickListener { onEditClick(movie) }
//            binding.deleteButton.setOnClickListener {
//                Log.d("MovieAdapter", "Deleting movie with ID: ${movie.id}")
//                onDeleteClick(movie.id) // Pass only the movie ID
//            }
//        }
//    }
//}
//

package com.example.moviesapp

//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.moviesapp.databinding.ItemMovieBinding

//class MovieAdapter(
//    private var movies: List<Movie>,
//    private val onMovieClick: (String?) -> Unit
//) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
//        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MovieViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
//        val movie = movies[position]
//        holder.bind(movie)
//    }
//
//    override fun getItemCount(): Int = movies.size
//
//    fun updateMovies(newMovies: List<Movie>) {
//        movies = newMovies
//        notifyDataSetChanged()
//    }
//
//    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(movie: Movie) {
//            binding.movieTitle.text = movie.title
//            Glide.with(binding.movieThumbnail.context)
//                .load(movie.posterUrl)
//                .placeholder(R.drawable.ic_launcher_background)
//                .into(binding.movieThumbnail)
//
//            binding.root.setOnClickListener {
//                onMovieClick(movie.id)
//            }
//        }
//    }
//}

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.databinding.ItemMovieBinding

class MovieAdapter(
    private var movies: List<Movie>,
    private val onMovieClick: (String?, Double, String?) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            onMovieClick(movie.id, movie.rating, movie.studio)
        }
    }

    override fun getItemCount() = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        val diffCallback = MovieDiffCallback(movies, newMovies)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        movies = newMovies
        diffResult.dispatchUpdatesTo(this)
    }

    class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieTitle.text = "Title: ${movie.title}"
            binding.movieStudio.text = "Production: ${movie.studio ?: "Unknown"}"
            binding.movieRating.text = "Rating: ${movie.rating}"
        }
    }

    class MovieDiffCallback(
        private val oldList: List<Movie>,
        private val newList: List<Movie>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}

