package com.example.moviesapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class MovieViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    fun fetchMovies() {
        db.collection("movies").get()
            .addOnSuccessListener { snapshot ->
                val movieList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject<Movie>()?.copy(id = doc.id)
                }
                _movies.value = movieList
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching movies", e)
            }
    }

    fun getMovieById(movieId: String): LiveData<Movie?> {
        val movieData = MutableLiveData<Movie?>()
        if (movieId.isBlank() || movieId.contains("/")) {
            Log.e("Firestore", "Invalid movie ID: $movieId")
            movieData.value = null
            return movieData
        }
        db.collection("movies").document(movieId).get()
            .addOnSuccessListener { document ->
                movieData.value = document.toObject<Movie>()?.copy(id = document.id)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching movie by ID: $movieId", e)
                movieData.value = null
            }
        return movieData
    }

    fun addMovie(title: String, studio: String, rating: Double) {
        if (title.isBlank() || studio.isBlank()) {
            Log.e("Firestore", "Invalid movie parameters")
            return
        }
        val movie = Movie(title = title, studio = studio, rating = rating)
        db.collection("movies").add(movie)
            .addOnSuccessListener {
                Log.d("Firestore", "Movie added successfully")
                fetchMovies()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding movie", e)
            }
    }

    fun updateMovie(movieId: String, title: String, studio: String, rating: Double) {
        if (movieId.isBlank() || movieId.contains("/") || title.isBlank() || studio.isBlank()) {
            Log.e("Firestore", "Invalid parameters for movie update: $movieId")
            return
        }
        val movieData = mapOf(
            "title" to title,
            "studio" to studio,
            "rating" to rating
        )
        db.collection("movies").document(movieId).update(movieData)
            .addOnSuccessListener {
                Log.d("Firestore", "Movie updated successfully")
                fetchMovies()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating movie: $movieId", e)
            }
    }

    fun deleteMovie(movieId: String) {
        if (movieId.isBlank() || movieId.contains("/")) {
            Log.e("Firestore", "Invalid movie ID for deletion: $movieId")
            return
        }
        db.collection("movies").document(movieId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Movie deleted successfully: $movieId")
                fetchMovies()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error deleting movie: $movieId", e)
            }
    }
}
