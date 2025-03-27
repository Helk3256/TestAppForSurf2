package com.example.TestAppForSurf

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Book::class], version = 1)
@TypeConverters(ImageLinksConverter::class, Converters::class)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}