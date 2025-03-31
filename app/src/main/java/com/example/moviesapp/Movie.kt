package com.example.moviesapp
import java.io.Serializable

data class Movie(
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val posterUrl: String? = null,
    val isFavorite: Boolean = false,
    val rating: Double = 0.0,
    val studio: String = ""
) : Serializable

//data class Movie(
//    val id: String = "",
//    val title: String = "",
//    val studio: String = "",
//    val rating: Double = 0.0,
//    val posterUrl: String = ""
//)

