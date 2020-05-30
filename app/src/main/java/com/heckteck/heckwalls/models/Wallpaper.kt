package com.heckteck.heckwalls.models

import com.google.firebase.Timestamp

data class Wallpaper(
    val name: String = "",
    val image: String = "",
    val thumbnail: String = "",
    val date: Timestamp? = null
)