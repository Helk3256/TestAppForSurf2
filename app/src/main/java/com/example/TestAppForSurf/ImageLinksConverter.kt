package com.example.TestAppForSurf

import androidx.room.TypeConverter
import com.google.gson.Gson

class ImageLinksConverter {
    @TypeConverter
    fun fromImageLinks(imageLinks: ImageLinks?): String? {
        return Gson().toJson(imageLinks)
    }

    @TypeConverter
    fun toImageLinks(imageLinksString: String?): ImageLinks? {
        return Gson().fromJson(imageLinksString, ImageLinks::class.java)
    }
}