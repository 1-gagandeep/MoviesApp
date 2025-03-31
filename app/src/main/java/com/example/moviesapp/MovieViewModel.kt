package com.example.moviesapp

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
                val movieList = snapshot.documents.mapNotNull { it.toObject<Movie>()?.copy(id = it.id) }
                _movies.value = movieList
            }
    }

    fun getMovieById(movieId: String): LiveData<Movie?> {
        val movieData = MutableLiveData<Movie?>()
        db.collection("movies").document(movieId).get()
            .addOnSuccessListener { document ->
                movieData.value = document.toObject<Movie>()?.copy(id = document.id)
            }
        return movieData
    }

    fun addMovie(title: String, studio: String, rating: Double) {
        val movie = Movie(title = title, studio = studio, rating = rating)
        db.collection("movies").add(movie)
            .addOnSuccessListener { fetchMovies() }
    }

    fun updateMovie(movieId: String, title: String, studio: String, rating: Double) {
        val movieData = mapOf(
            "title" to title,
            "studio" to studio,
            "rating" to rating
        )
        db.collection("movies").document(movieId).update(movieData)
            .addOnSuccessListener { fetchMovies() }
    }

    fun deleteMovie(movieId: String) {
        db.collection("movies").document(movieId).delete()
            .addOnSuccessListener { fetchMovies() }
    }
}
