package com.example.TestAppForSurf

import android.app.Application
import androidx.room.Room

class MyApplication : Application() {

    lateinit var database: BookDatabase


    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            BookDatabase::class.java,
            "book_database"
        ).build()
    }
}