package com.example.TestAppForSurf

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Book::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class, ImageLinksConverter::class)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Use applicationContext
                    BookDatabase::class.java,
                    "book_database" // Database name
                )
                    .fallbackToDestructiveMigration() // Add migration strategy
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}