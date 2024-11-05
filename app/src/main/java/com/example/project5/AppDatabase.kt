package com.example.project5

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WaterEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun waterEntryDao(): WaterEntryDao
}
